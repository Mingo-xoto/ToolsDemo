package saber;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import saber.interfaces.A;
import saber.interfaces.C;
import saber.lambda.Person;
import saber.lambda.impl.ConverterImpl;
import saber.lambda.interfaces.Converter;
import saber.lambda.interfaces.PersonFactory;

public class B {
	/**
	 * @author HuaQi.Yang
	 * @date 2017Äê5ÔÂ16ÈÕ
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
