package com.sportal.util;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sportal.model.User;



public class UserValidator implements Validator {

	public boolean supports(Class<?> paramClass) {
		return User.class.equals(paramClass);
	}

	public void validate(Object obj, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "valid.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "valid.lastName");
	}
}	
	
