package com.wictor.util;

public class EmailValidator {

    public static boolean validar(String email) {

        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");


    }

}