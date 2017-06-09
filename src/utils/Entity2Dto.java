package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.Map;

public class Entity2Dto {

	final static String entityName = "ServiceProvider";
	final static String fileType = ".java";
	final static String dtoPackage = "cn#paywe#fos#api#dto#manage";
	final static String entityPackage = "cn#paywe#fos#api#repository#manage#entity#";
	final static String dtoPath = "E:/Git/MyRepository/fos-api-item/fos-api-beans/src/main/java/" + dtoPackage.replaceAll("#", "/") + "/";
	final static String entityPath = "E:/Git/MyRepository/fos-api-item/fos-api-repository/src/main/java/" + entityPackage.replaceAll("#", "/") + entityName + fileType;

	public static String getDtoFileName() {
		return entityName + "Dto" + fileType;
	}

	public static String getDtoName() {
		return entityName + "Dto";
	}

	/**
	 * entity-->dto
	 * 
	 * @throws IOException
	 */
	public static void transefer() throws IOException {
		File file = new File(entityPath);
		FileInputStream fis = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis, "GBK"));
		FileOutputStream fos = new FileOutputStream(new File(dtoPath + getDtoFileName()));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "GBK"));
		String line = null;
		line = br.readLine();
		if (line != null) {
			// 修改package
			if (line.startsWith("package")) {
				bw.write("package " + dtoPackage.replaceAll("#", ".") + ";");
			}
		}
		while ((line = br.readLine()) != null) {
			String tmp = line.trim();
			if (tmp.startsWith("@") || tmp.startsWith("/**") || tmp.startsWith("*")) {
				continue;
			}
			if (tmp.indexOf("extends DomainBase") >= 0) {
				String innerLine = line.replaceAll("extends DomainBase", "extends DtoBase");
				innerLine = innerLine.replaceAll(entityName, getDtoName());
				bw.write(innerLine + "\n");
			} else {
				bw.write(line.replaceAll(entityName, getDtoName()) + "\n");
			}
		}
		bw.close();
		br.close();
		fis.close();
		fos.close();
	}

	public static void main(String[] args) {
		try {
			// entity转dto
			transefer();
			// 查询数据库表字段信息给dto属性添加注释
			Map<String, TableInfo> tableInfoMap = TableUtils.getAllTableInfo();
			File dir = new File(dtoPath);
			TableUtils.listFile(dir, tableInfoMap);
			// 生成接口文档返回参数列表
			ParameterDescriptionUtils.createReturnDataStructor(dtoPath, getDtoFileName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
