package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 实体参数说明生成工具
 * 
 * @author HuaQi.Yang
 * @date 2017年6月7日
 */
public class ParameterDescriptionUtils {
	final static String ROOTPATH = "E:/workspace1/saber/src/file";
	final static int DIFFERENCE = 'A' - 'a';

	public static Template getTemplate(String name) {
		try {
			File file = new File(ROOTPATH);
			Configuration cfg = new Configuration();
			cfg.setDirectoryForTemplateLoading(file);
			Template temp = cfg.getTemplate(name);
			return temp;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String fprint(String name, Map<String, Object> map, String outFile) {
		FileWriter out = null;
		String buffer = null;
		try {
			// 通过一个文件输出流，就可以写到相应的文件中
			out = new FileWriter(new File(ROOTPATH + "/" + outFile));
			Template temp = getTemplate(name);
			temp.process(map, out);
			buffer = getTemplate(outFile).toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return buffer;
	}

	public static void main(String[] args) throws IOException {
		// 读取从数据库导出的表字段注释
		Map<String, String> commentMap = createPropertyCommentMap();
		// StringBuilder trs = readPropertyListOfBean(commentMap);
		StringBuilder trs = readFullPropertyListOfBean();
		Map<String, Object> map = new HashMap<>();
		map.put("trs", trs.toString());
		// 1、创建数据模型
		// 3、将数据模型和模板组合的数据输出到控制台
		fprint("ParameterDescription.ftl", map, "返回参数.doc");
	}

	/**
	 * 读取bean的属性列表集合
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年6月7日
	 * @param commentMap
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static StringBuilder readPropertyListOfBean(Map<String, String> commentMap) throws FileNotFoundException, IOException {
		String line = null;
		String rp = "E:/Git/MyRepository/fos-api-item/fos-api-beans/src/main/java/cn/paywe/fos/api/dto/";
		// 读取返回实体dto的字段列表
		String path = rp + "manage/MerchantDto.java";
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		StringBuilder trs = new StringBuilder();
		while ((line = br.readLine()) != null) {
			line = line.trim();
			// 只筛选字段行
			if ((line.startsWith("private") || line.startsWith("public")) && !line.endsWith("{") && line.indexOf("=") < 0) {
				String arrs[] = line.split(" ");
				Map<String, Object> map = new HashMap<String, Object>();
				// 2、为数据模型添加值
				String property = arrs[2].split(";")[0];
				map.put("property", property);
				map.put("type", getType(arrs[1]));
				String description = commentMap.get(property);
				map.put("description", description == null ? "" : description);
				trs.append(fprint("tr.ftl", map, "tr_new.ftl"));
			}
		}
		br.close();
		return trs;
	}

	public static StringBuilder readFullPropertyListOfBean() throws FileNotFoundException, IOException {
		String line = null;
		String rp = "E:/Git/MyRepository/fos-api-item/fos-api-beans/src/main/java/cn/paywe/fos/api/dto/";
		// 读取返回实体dto的字段列表
		String path = rp + "manage/MerchantDto.java";
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		StringBuilder trs = new StringBuilder();
		while ((line = br.readLine()) != null) {
			line = line.trim();
			// 只筛选字段行
			if ((line.startsWith("private") || line.startsWith("public")) && !line.endsWith("{") && line.indexOf("=") < 0) {
				String arrs[] = line.split(" ");
				Map<String, Object> map = new HashMap<String, Object>();
				// 2、为数据模型添加值
				String property = arrs[2].split(";")[0];
				map.put("property", property);
				map.put("type", getType(arrs[1]));
				String infos[] = arrs[2].split(";");
				String description = infos == null || infos.length < 2 ? "" : infos[1].replaceAll("//", "");
				map.put("description", description);
				trs.append(fprint("tr.ftl", map, "tr_new.ftl"));
			}
		}
		br.close();
		return trs;
	}

	public static String getType(String type) {
		String finalType = "";
		switch (type) {
		case "Short":
			;
		case "Byte":
			;
		case "Integer":
			finalType = "int";
			break;
		default:
			finalType = type.toLowerCase();
		}
		return finalType;
	}

	/**
	 * 生成字段注释映射表
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年6月7日
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Map<String, String> createPropertyCommentMap() throws IOException {
		String commentPath = "E:/workspace1/saber/src/file/comments.xml";
		File file = new File(commentPath);
		FileInputStream fis;
		BufferedReader br = null;
		Map<String, String> commentMap = new HashMap<>();
		try {
			fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis));
			String line = null;
			while ((line = br.readLine()) != null) {
				String arrs[] = line.split("\t");
				if (arrs.length > 1) {
					String key = dataBaseNaming2JavaNaming(arrs[0], false);
					commentMap.put(key, arrs[arrs.length - 1]);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				br.close();
			}
		}
		return commentMap;
	}

	/**
	 * 数据库命名转java命名
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年6月7日
	 * @param scape
	 * @param arrs
	 * @return
	 */
	public static String dataBaseNaming2JavaNaming(String dataName, boolean isTable) {
		StringBuilder key = new StringBuilder();
		if (isTable) {
			key.append((char) (dataName.charAt(0) + DIFFERENCE));
		} else {
			key.append(dataName.charAt(0));
		}
		for (int i = 1, length = dataName.length(); i < length; ++i) {
			char ch = dataName.charAt(i);
			if (ch == '_') {
				ch = (char) (dataName.charAt(++i) + DIFFERENCE);
			}
			key.append(ch);
		}
		return key.toString();
	}

	public static void rewriteDto() throws IOException {
		String commentPath = "E:/workspace/saber/src/file/FOS_Manege.txt";
		File file = new File(commentPath);
		FileInputStream fis = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis, "GBK"));
		String line = null;
		Map<String, Map<String, String>> tabCommentMap = new HashMap<>();

		try {
			String tempTab = "";
			while ((line = br.readLine()) != null) {
				String arrs[] = line.split("\t");
				if (arrs.length > 1) {
					String key;
					if (arrs[1].startsWith("Table")) {
						key = dataBaseNaming2JavaNaming(arrs[1].replace("Table", "").trim(), true);
						Map<String, String> commentMap = new HashMap<>();
						tabCommentMap.put(key, commentMap);
						tempTab = key;
						br.readLine();
						br.readLine();
					} else {
						key = dataBaseNaming2JavaNaming(arrs[0], false);
						Map<String, String> commentMap = tabCommentMap.get(tempTab);
						if ("platformName".equals(key)) {
							System.out.println();
						}
						commentMap.put(key, arrs[arrs.length - 1]);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			fis.close();
			br.close();
		}
		for (Entry<String, Map<String, String>> entry : tabCommentMap.entrySet()) {
			System.out.println("table:" + entry.getKey());
			Map<String, String> commentMap = entry.getValue();
			for (Entry<String, String> entry1 : commentMap.entrySet()) {
				System.out.println(entry1.getKey() + ":" + entry1.getValue());
			}
		}
	}

}
