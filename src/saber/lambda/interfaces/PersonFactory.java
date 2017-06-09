package saber.lambda.interfaces;

import saber.lambda.Person;

/**
 * @author HuaQi.Yang
 * @date 2017Äê5ÔÂ16ÈÕ
 */
public interface PersonFactory<P extends Person> {
    P create(String firstName, String lastName);
}