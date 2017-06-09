package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.TableInfo.ColumnInfo;

/**
 * 表工具
 * 
 * @author HuaQi.Yang
 * @date 2017年6月8日
 */
public class TableUtils {

	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection("jdbc:sqlserver://10.1.20.57:1433;databaseName=FOS_Manage;&characterEncoding=GBK", "FOS_Admin", "FOS_AdminAdmin");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 获取当前数据库下的所有表名称
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<String> getAllTableName() throws Exception {
		List<String> tables = new ArrayList<String>();
		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT Name FROM SysObjects Where XType='U' ORDER BY Name");
		while (rs.next()) {
			String tableName = rs.getString(1);
			tables.add(tableName);
		}
		rs.close();
		stmt.close();
		conn.close();
		return tables;
	}

	public static List<String> getColumnOfTable(List<String> tables) throws SQLException {
		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		for (int i = 0; i < tables.size(); i++) {
			String table = tables.get(i);
			ResultSet rs = stmt.executeQuery("SELECT name  FROM SysObjects Where XType='U' ORDER BY Name;");
			System.out.println("【" + table + "】");
			// if (rs != null && rs.next()) {
			// map.put(rs.getString("Field"), rs.getString("Comment"));
			while (rs.next()) {
				// System.out.println("字段名称：" + rs.getString("Field") + "\t"+
				// "字段注释：" + rs.getString("Comment") );
				System.out.println(rs.getString("name") + "\t:\t" + rs.getString("Comment"));
			}
			// }
			rs.close();
		}
		stmt.close();
		conn.close();
		return tables;
	}

	/**
	 * 获取指定表的各个字段类型和注释
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public static Map<String, String> getColumnCommentOfTable(String tableName) throws SQLException {
		Map<String, String> commentMap = new HashMap<>();
		StringBuilder sql = new StringBuilder("SELECT s_c.name as colName,s_t.name as type,cast(e_p.value as varchar(50)) as comment  FROM SysColumns s_c\n");
		sql.append("JOIN sys.extended_properties e_p on s_c.colid=e_p.minor_id and s_c.id=e_p.major_id\n");
		sql.append("JOIN systypes s_t on s_t.xusertype=s_c.xusertype\n");
		sql.append("left join sys.extended_properties f on d.id=f.major_id and f.minor_id=0\n");
		sql.append("where s_c.id=Object_Id('");
		sql.append(tableName);
		sql.append("');");
		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql.toString());
		while (rs.next()) {
			System.out.println(rs.getString("colName") + "\t:" + rs.getString("type") + "\t:" + rs.getString("comment"));
		}
		return commentMap;
	}

	/**
	 * 查询数据库表的字段、字段类型、字段注释以及表注释
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Map<String, TableInfo> getAllTableInfo() throws SQLException {
		Map<String, TableInfo> tableInfoMap = new HashMap<>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT t.name as tableName,cast(f.value as varchar(50)) as tableComment,c.name as colName,s_t.name as colType,cast(e_p.value as varchar(50)) as colComment\n");
		sql.append("FROM SysObjects t JOIN SysColumns c on t.id=c.id\n");
		sql.append("left JOIN sys.extended_properties e_p on c.colid=e_p.minor_id and c.id=e_p.major_id\n");
		sql.append("left JOIN systypes s_t on s_t.xusertype=c.xusertype\n");
		sql.append("left join sys.extended_properties f on t.id=f.major_id and f.minor_id=0");
		sql.append("where t.XType='U' ");
		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql.toString());
		while (rs.next()) {
			String tableName = rs.getString("tableName");
			String key = ParameterDescriptionUtils.dataBaseNaming2JavaNaming(tableName, true);
			TableInfo tableInfo;
			HashMap<String, ColumnInfo> cols;
			if (tableInfoMap.containsKey(key)) {
				tableInfo = tableInfoMap.get(key);
				cols = tableInfo.getCols();
			} else {
				tableInfo = new TableInfo();
				tableInfo.setTableName(tableName);
				tableInfo.setTableComment(rs.getString("tableComment"));
				cols = new HashMap<String, ColumnInfo>();
				tableInfo.setCols(cols);
				tableInfoMap.put(key, tableInfo);
			}
			ColumnInfo col = new TableInfo().getColumnInfo();
			col.setColName(rs.getString("colName"));
			cols.put(ParameterDescriptionUtils.dataBaseNaming2JavaNaming(col.getColName(), false), col);
			col.setColType(rs.getString("colType"));
			col.setColComment(rs.getString("colComment"));
		}
		return tableInfoMap;
	}

	public static void main(String[] args) throws Exception {
		Map<String, TableInfo> tableInfoMap = getAllTableInfo();

		File dir = new File("E:/Git/MyRepository/fos-api-item/fos-api-beans/src/main/java/cn/paywe/fos/api/dto");
		listFile(dir, tableInfoMap);
	}

	/**
	 * 遍历文件
	 * 
	 * @param file
	 * @param tableInfoMap
	 */
	public static void listFile(File file, Map<String, TableInfo> tableInfoMap) {
		if (file.isDirectory()) {
			File files[] = file.listFiles();
			for (File f : files) {
				listFile(f, tableInfoMap);
			}
		} else {
			try {
				String key = file.getName().replaceAll("Dto.java", "");
				if (key.equals("AgencyDetail") || key.equals("AgencyEdit")) {
					key = "Agency";
				}
				TableInfo tableInfo = tableInfoMap.get(key);
				if (tableInfo == null) {
					return;
				}
				rewriteJavaFile(tableInfo, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 添加注释
	 * 
	 * @param tableInfo
	 * @param file
	 * @throws IOException
	 */
	public static void rewriteJavaFile(TableInfo tableInfo, File file) throws IOException {
		String oldName = file.getAbsolutePath();
		String infos[] = file.getName().split("\\.");
		File newFile = new File(file.getParent() + File.separator + infos[0] + "New." + infos[1]);
		FileInputStream fis = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
		FileOutputStream fos = new FileOutputStream(newFile);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
		String line = null;
		HashMap<String, ColumnInfo> cols = tableInfo.getCols();
		while ((line = br.readLine()) != null) {
			String line1 = line.trim();
			// System.out.println(line);
			// 只筛选字段行
			if ((line1.startsWith("private") || line1.startsWith("public")) && !line1.endsWith("{") && line1.indexOf("=") < 0) {
				String arrs[] = line1.split(" ");
				if (arrs.length > 2) {
					ColumnInfo col = cols.get(arrs[2].split(";")[0]);
					// 去掉没有真实注释的注释标记“//”
					if (line.endsWith("//") || line.endsWith("//\n")) {
						line = line.replaceAll("//", "");
					}
					if (col != null && line.indexOf("//") < 0) {
						if (col.getColComment() != null && col.getColComment().length() > 0) {
							line += "// " + col.getColComment();
						}
					}
				}
			}
			bw.write(line + "\n");
		}
		br.close();
		fis.close();
		bw.close();
		fos.close();
		file.delete();
		newFile.renameTo(new File(oldName));
	}

}
