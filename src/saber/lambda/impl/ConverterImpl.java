package saber.lambda.impl;

import java.math.BigDecimal;

import saber.lambda.interfaces.Converter;

/**
 * @author HuaQi.Yang
 * @date 2017Äê5ÔÂ16ÈÕ
 */
public class ConverterImpl {

	public static Converter<String, BigDecimal> s_b_convert = (fuck) -> new BigDecimal(fuck);

	public static Converter<String, Integer> converter = (from) -> Integer.valueOf(from);
}
