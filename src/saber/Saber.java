package saber;

import java.lang.reflect.Constructor;

public class Saber {

	public int a;
	public String b;
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + a;
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Saber other = (Saber) obj;
		if (a != other.a)
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		return true;
	}

	public Saber() {
		System.out.println("1");
	}

	public Saber(Saber saber) {
		System.out.println("2");
	}

	public static void main(String[] args) throws NoSuchMethodException, SecurityException {
		float a = 1.0f;
		int b = 1;
		System.out.println(a - b);
		Class<?> intClass = int.class;
		intClass = double.class;
		System.out.println(intClass.getName());
		intClass = Saber.class;
		System.out.println(intClass.getName());
		Saber saber = new Saber();
		if (saber instanceof Saber) {
			System.out.println("---");
		}
		if (saber instanceof Saber) {
			System.out.println("---");
		}
		if (Saber.class.isInstance(saber)) {
			System.out.println("---");
		}
		System.out.println(saber);
		Constructor csr = intClass.getConstructor(Saber.class);
		System.out.println(csr.getName());
		Constructor csrs[] = intClass.getConstructors();
		for (Constructor constructor : csrs) {
			System.out.print(constructor.getName()+":"+constructor.getParameterCount()+"----->");
			Class tcs[] = constructor.getParameterTypes();
			for (Class class1 : tcs) {
				System.out.print(class1.getName());
			}
			System.out.println();
		}
	}
}
