package com.sportal.model;

public interface IValid {

	public static boolean varchar(String str, int Lenght) {
		return str!=null && !str.trim().isEmpty() && str.length() < Lenght;
	}
}
