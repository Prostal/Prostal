package com.sportal.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Encrypter {

	private Encrypter(){}
	
	public static String encrypt(String text){
		 MessageDigest md = null;
		try {
			md = MessageDigest.getInstance( "SHA-512" );
		} catch (NoSuchAlgorithmException e) {
			System.out.println("op");
		}
		if(md==null) {
			return text;
		}
	    md.update(text.getBytes());
	    byte[] aMessageDigest = md.digest();

	    String outEncoded = Base64.getEncoder().encodeToString( aMessageDigest );
	    return( outEncoded );
		
	}
}
