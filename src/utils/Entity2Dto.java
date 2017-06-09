package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Entity2Dto {

	public static void transefer() throws IOException {
		final String entityName = "ServiceProvider.java";
		final String dtoPackage = "cn#paywe#fos#api#dto#manage#";
		final String entityPackage = "cn#paywe#fos#api#repository#manage#entity#";
		final String dtoPath = "E:/Git/MyRepository/fos-api-item/fos-api-beans/src/main/java/" + dtoPackage.replaceAll("#", "/");
		final String entityPath = "E:/Git/MyRepository/fos-api-item/fos-api-repository/src/main/java/" + entityPackage.replaceAll("#", "/") + entityName;
		File file = new File(entityPath);
		String fileInfo[] = file.getName().split("\\.");
		FileInputStream fis = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis, "GBK"));
		FileOutputStream fos = new FileOutputStream(new File(dtoPath + fileInfo[0] + "Dto." + fileInfo[1]));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "GBK"));
		String line = null;
		line = br.readLine();
		if (line != null) {
			if (line.startsWith("package")) {
				bw.write("package " + dtoPackage.replaceAll("#", "."));
			}
		}
		while ((line = br.readLine()) != null) {
			String tmp = line.trim();
			if (tmp.startsWith("@") || tmp.startsWith("/**") || tmp.startsWith("*")) {
				continue;
			}
			if (tmp.indexOf("extends DomainBase") >= 0) {
				bw.write(line.replaceAll("extends DomainBase", "extends DtoBase") + "\n");
			} else {
				bw.write(line + "\n");
			}
		}
		bw.close();
		br.close();
		fis.close();
		fos.close();
	}

	public static void main(String[] args) {
		try {
			transefer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
