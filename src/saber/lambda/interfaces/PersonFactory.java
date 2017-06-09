package saber.lambda.interfaces;

import saber.lambda.Person;

/**
 * @author HuaQi.Yang
 * @date 2017��5��16��
 */
public interface PersonFactory<P extends Person> {
    P create(String firstName, String lastName);
}