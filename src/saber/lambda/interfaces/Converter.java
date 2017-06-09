package saber.lambda.interfaces;

/**
 * @author HuaQi.Yang
 * @date 2017Äê5ÔÂ16ÈÕ
 */
@FunctionalInterface
public interface Converter<From, To> {
	To convert(From from);
	
	public static void s() {
	}
	
	default void show(){
		
	}
	
	default void show1(){
	}
}
