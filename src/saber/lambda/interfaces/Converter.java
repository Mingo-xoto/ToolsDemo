package saber.lambda.interfaces;

/**
 * @author HuaQi.Yang
 * @date 2017��5��16��
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
