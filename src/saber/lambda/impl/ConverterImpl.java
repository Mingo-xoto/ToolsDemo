package saber.lambda.impl;

import java.math.BigDecimal;

import saber.lambda.interfaces.Converter;

/**
 * @author HuaQi.Yang
 * @date 2017��5��16��
 */
public class ConverterImpl {

	public static Converter<String, BigDecimal> s_b_convert = (fuck) -> new BigDecimal(fuck);

	public static Converter<String, Integer> converter = (from) -> Integer.valueOf(from);
}
