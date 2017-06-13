package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

	private static List<String[]> childBeans = new ArrayList<String[]>();
	private static Map<String, String> dtoNameMap = new HashMap<>();

	public static void main(String[] args) throws IOException {
		final String path = "E:/Git/MyRepository/fos-api-item/fos-api-beans/src/main/java/cn/paywe/fos/api/dto/";
		StringBuilder trs = new StringBuilder();
		trs.append(createDataStructor(path + "manage/", "AgencyDetailDto.java"));
		if (childBeans.size() > 0) {
			createInRecursion(trs);
		}
		printFile("AgencyDetailDto.java", trs.toString());
	}

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

	/**
	 * freemarker模板生成文件
	 * 
	 * @param name
	 * @param map
	 * @param outFile
	 * @return
	 */
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

	public static void listFile(File file) {
		if (file.isDirectory()) {
			File files[] = file.listFiles();
			for (File f : files) {
				listFile(f);
			}
		} else {
			dtoNameMap.put(file.getName().split("\\.")[0], file.getParent() + File.separator);
		}

	}

	public static void addTitle(StringBuilder trs) {
		trs.append(getTemplate("title.ftl").toString());
	}

	/**
	 * 递归生成dto属性注释：dto属性也为自定义dto类
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static String createInRecursion(StringBuilder trs) throws IOException, FileNotFoundException {
		List<String[]> childBeans = ParameterDescriptionUtils.childBeans;
		ParameterDescriptionUtils.childBeans = new ArrayList<String[]>();
		for (String childBean[] : childBeans) {
			if (dtoNameMap.containsKey(childBean[0])) {
				trs.append("</w:tbl><w:p w:rsidR=\"007A2174\" w:rsidRDefault=\"007A2174\"><w:r><w:rPr><w:rFonts w:hint=\"eastAsia\" /></w:rPr><w:t>");
				trs.append(childBean[0]+":");
				addTitle(trs);
				trs.append(readFullPropertyListOfBean(childBean[1], childBean[0] + ".java"));
			}
			// 同一个dto属性信息只输出一次
			dtoNameMap.remove(childBean[0]);
		}
		if (childBeans.size() > 0) {

			createInRecursion(trs);
		}
		return trs.toString();
	}

	/**
	 * 生成dto属性注释文档
	 * 
	 * @param path
	 * @param dto
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static String createDataStructor(String path, String dto) throws IOException, FileNotFoundException {
		File dir = new File(path);
		listFile(dir);
		return readFullPropertyListOfBean(path, dto);
	}

	/**
	 * 生成单一dto属性注释文档
	 * 
	 * @param rp
	 * @param dtoName
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static String createSingleDataStructor(String rp, String dtoName) throws IOException, FileNotFoundException {
		// 读取从数据库导出的表字段注释
		String trs = readFullPropertyListOfBean(rp, dtoName);
		printFile(dtoName, trs);
		return trs;
	}

	/**
	 * 输出文件
	 * 
	 * @param dtoName
	 * @param trs
	 */
	private static void printFile(String dtoName, String trs) {
		Map<String, Object> map = new HashMap<>();
		map.put("trs", trs.toString());
		// 1、创建数据模型
		// 3、将数据模型和模板组合的数据输出到控制台
		fprint("ParameterDescription.ftl", map, dtoName.split("\\.")[0] + ".doc");
	}

	public static String readFullPropertyListOfBean(String rp, String dtoName) throws FileNotFoundException, IOException {
		String path = rp + dtoName;
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		StringBuilder trs = new StringBuilder();
		readPropertyLine(br, trs);
		br.close();
		return trs.toString();
	}

	/**
	 * 读取属性行
	 * 
	 * @param br
	 * @param trs
	 * @throws IOException
	 */
	private static void readPropertyLine(BufferedReader br, StringBuilder trs) throws IOException {
		String line;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			// 只筛选字段行
			if ((line.startsWith("private") || line.startsWith("public")) && !line.endsWith("{") && line.indexOf("=") < 0) {
				String arrs[] = line.split(" ");
				Map<String, Object> map = new HashMap<String, Object>();
				// 2、为数据模型添加值
				String infos[] = arrs[2].split(";");
				String property = infos[0];
				map.put("property", property);
				map.put("type", matchUserDefinedBean(arrs[1]));
				String description = arrs.length > 3 ? arrs[arrs.length - 1] : "";
				if ("".equals(description)) {
					map.put("description", infos.length > 1 ? infos[1].replaceAll("//", "") : "");
				} else {
					map.put("description", description);
				}
				trs.append(fprint("tr.ftl", map, "tr_new.ftl"));
				// BufferedReader n_br =
				// getReaderAfterMatchUserDefinedBean(type);
				// if (n_br != null) {
				// readPropertyLine(n_br, trs);
				// }
			}
		}
	}

	@SuppressWarnings("unused")
	private static BufferedReader getReaderAfterMatchUserDefinedBean(String type) throws FileNotFoundException {
		for (String beanName : variableDetection(type)) {
			if (dtoNameMap.containsKey(beanName)) {
				String dtoPath = dtoNameMap.get(beanName) + beanName + ".java";
				dtoNameMap.remove(beanName);
				return new BufferedReader(new InputStreamReader(new FileInputStream(new File(dtoPath))));
			}
		}
		return null;
	}

	/**
	 * 匹配是否为用户自定义类
	 * 
	 * @param type
	 */
	private static String matchUserDefinedBean(String type) {
		String finalType = getType(type);
		for (String beanName : variableDetection(type)) {
			if (dtoNameMap.containsKey(beanName)) {
				childBeans.add(new String[] { beanName, dtoNameMap.get(beanName) });
				if (beanName.toLowerCase().equals(finalType)) {
					return beanName;
				} else {
					return beanName + "对象" + finalType;
				}
			}
		}
		return finalType;
	}

	/**
	 * 变量检测
	 * 
	 * @param type
	 * @return
	 */
	private static List<String> variableDetection(String type) {
		List<String> beanNames = new ArrayList<String>();
		StringBuilder word = new StringBuilder();
		for (int i = 0, length = type.length(); i < length; ++i) {
			char letter = type.charAt(i);
			// 满足为变量命名规则：首字母是英文字母、$和下划线，由字母、数字和下划线组成
			if ((letter >= 'a' && letter <= 'z') || (letter >= 'A' && letter <= 'Z') || letter == '_' || letter == '$' || (letter <= 9 && letter >= 0)) {
				word.append(letter);
			} else {
				beanNames.add(word.toString());
				word = new StringBuilder();
			}
		}
		beanNames.add(word.toString());
		return beanNames;
	}

	public static String getType(String type) {
		String finalType = "";
		switch (type) {
		case "Short":
		case "Byte":
		case "Integer":
			return "int";
		default:
			finalType = type.toLowerCase();
			finalType = finalType.replaceAll("<[a-zA-Z]+>", "");
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
