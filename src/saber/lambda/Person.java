package saber.lambda;

import saber.lambda.interfaces.PersonFactory;

/**
 * @author HuaQi.Yang
 * @date 2017��5��16��
 */
public class Person {
	String firstName;
	String lastName;

	Person() {
	}

	Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public static void main(String[] args) {
		PersonFactory<Person> personFactory = Person::new;
		Person person = personFactory.create("Peter", "Parker");
		System.out.println(person.firstName);
	}
}
