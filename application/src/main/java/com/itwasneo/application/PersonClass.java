package com.itwasneo.application;

import java.io.Serializable;

public class PersonClass implements Serializable {
	public String name;
	public int age;

	public PersonClass(
			String name,
			int age) {
		this.name = name;
		if (age > 20) {
			throw new IllegalArgumentException();
		}
		this.age = age;
	}

	public PersonClass() {
	}
}