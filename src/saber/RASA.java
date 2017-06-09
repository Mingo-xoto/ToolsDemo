package saber;

import java.util.HashMap;

/**
 * 
 * @author HuaQi.Yang
 * @date 2017年5月9日
 */
public class RASA {

	private int fields;
	private static int staticField = 0;

	/**
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年5月9日
	 * @param a
	 * @param b
	 */
	public void tests(int a, String b) {
		HashMap<Object, Object> map = new HashMap<Object, Object>();
		map.containsKey(null);
	}

	public static void test(String json) {
		RASA rasa = new RASA();
		rasa.fields = staticField;
		System.out.println(rasa.fields);
		rasa.tests(0, "");
		// data=eyJVc2VyTm8iOiJ0YzExOCIsIkNoYW5uZWxObyI6IlEwMDEiLCJPcHRUeXBlIjoiMTAwMiIsIlRpbWVTdGFtcCI6MTQ4NDkxNDY1MSwiSVAiOiIxMjcuMC4wLjEiLCJQdWJsaWNLZXlObyI6IjAwMDAifQ%3D%3D
		// &signdata=WE67wI4uir5cavMA1DkeJJ%2BuE3BV0TnmB89P327T82DIIVMWPItShtbVG52ndBnKLXT2Bw%2Fzz9PhQtHjMyYAOQkyFiospXQbI5B03hclm1T33AY9blgTZ496Z9ZHRh%2FmrOuo2%2BAt6tKcaiQzyz3DnVFZLby3Gi8xHhAlJApc03Y%3D
	}

	public static void main(String[] args) {
		// test("{\"UserNo\":\"tc118\",\"ChannelNo\":\"Q001\",\"OptType\":\"1002\",\"TimeStamp\":1484914651,\"IP\":\"127.0.0.1\",\"PublicKeyNo\":\"0000\"}");
		// 基数
		int baseNum = 3 * 11;
		// 公钥
		int keyE = 3;
		// 密钥
		int keyD = 7;
		// 未加密的数据
		long msg = 24L;
		// 加密后的数据
		long encodeMsg = rsa(baseNum, keyE, msg);
		// 解密后的数据
		long decodeMsg = rsa(baseNum, keyD, encodeMsg);

		System.out.println("加密前：" + msg);
		System.out.println("加密后：" + encodeMsg);
		System.out.println("解密后：" + decodeMsg);
	}

	/**
	 * 加密、解密算法
	 * 
	 * @param key
	 *            公钥或密钥
	 * @param message
	 *            数据
	 * @return
	 */
	public static long rsa(int baseNum, int key, long message) {
		if (baseNum < 1 || key < 1) {
			return 0L;
		}
		// 加密或者解密之后的数据
		long rsaMessage = 0L;

		// 加密核心算法
		rsaMessage = Math.round(Math.pow(message, key)) % baseNum;
		return rsaMessage;
	}
}
