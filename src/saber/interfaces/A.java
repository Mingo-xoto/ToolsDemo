package saber.interfaces;

public interface A {

	String get();

	default void show() {
		System.out.println(get());
	}
	default void show1(){
		
	}
}
