package com.nisum.challenge.util;

import com.nisum.challenge.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ValidationUtil {

    private static final String EMAIL_REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static String PASSWORD_REGEX;

    @Value("${password.regexp}")
    public void setPasswordRegex(String passwordRegex) {
        PASSWORD_REGEX = passwordRegex;
    }

    public static void validateEmail(String email) throws CustomException {
        if (!email.matches(EMAIL_REGEX)) {
            throw new CustomException("The email address does not have the correct pattern");
        }
    }

    public static void validatePassword(String password) throws CustomException {
        if (!password.matches(PASSWORD_REGEX)) {
            throw new CustomException("The password is not strong enough");
        }
    }
}

