package saber;

import saber.interfaces.A;

public class B {
	/**
	 * @author HuaQi.Yang
	 * @date 2017��5��16��
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		A a = new A() {

			@Override
			public String get() {
				return "I'm A instance";
			}

		};
		a.show();
	}
}
