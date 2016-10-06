package com.xyz;

public class TestAssert {
	public static void main(String[] args) {
		try {
			acquireFoo(10);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			System.out.println("Error");
		}
	}

	public static Foo acquireFoo(int id) throws Throwable {
		Foo result = null;
//		if (id > 50) {
//			result = Foo.read(id);
//		} else {
//			result = new Foo(id);
//		}
		assert result != null;
		return result;
	}

	public static class Foo {
		private int id;

		public Foo(int id) {
			this.id = id;
		}

		public static Foo read(int id) {
			return null;
		}
	}

}
