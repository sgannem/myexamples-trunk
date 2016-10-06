package com.xyz.model.beans;

public class Person {

	private String ssn;
	private String name;

	public Person(Builder builder) {
		this.ssn = builder.ssn;
		this.name = builder.name;
	}

	/**
	 * @return the ssn
	 */
	public String getSsn() {
		return ssn;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public static Builder builder() {
		return new Builder();
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Person [ssn=" + ssn + ", name=" + name + "]";
	}



	public static final class Builder {

		private String ssn;
		private String name;

		public Builder() {
			// empty.
		}

		public Builder setSSN(String ssn) {
			this.ssn = ssn;
			return this;
		}

		public Builder setName(String name) {
			this.name = name;
			return this;
		}

		public Person build() {
			return new Person(this);
		}

	}

}
