package saber;

import java.util.HashMap;

/**
 * 
 * @author HuaQi.Yang
 * @date 2017��5��9��
 */
public class RASA {

	private int fields;
	private static int staticField = 0;

	/**
	 * 
	 * @author HuaQi.Yang
	 * @date 2017��5��9��
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
		// ����
		int baseNum = 3 * 11;
		// ��Կ
		int keyE = 3;
		// ��Կ
		int keyD = 7;
		// δ���ܵ�����
		long msg = 24L;
		// ���ܺ������
		long encodeMsg = rsa(baseNum, keyE, msg);
		// ���ܺ������
		long decodeMsg = rsa(baseNum, keyD, encodeMsg);

		System.out.println("����ǰ��" + msg);
		System.out.println("���ܺ�" + encodeMsg);
		System.out.println("���ܺ�" + decodeMsg);
	}

	/**
	 * ���ܡ������㷨
	 * 
	 * @param key
	 *            ��Կ����Կ
	 * @param message
	 *            ����
	 * @return
	 */
	public static long rsa(int baseNum, int key, long message) {
		if (baseNum < 1 || key < 1) {
			return 0L;
		}
		// ���ܻ��߽���֮�������
		long rsaMessage = 0L;

		// ���ܺ����㷨
		rsaMessage = Math.round(Math.pow(message, key)) % baseNum;
		return rsaMessage;
	}
}
