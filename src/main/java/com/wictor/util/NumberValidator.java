package com.wictor.util;

public class NumberValidator {

    public static boolean validar(String tel) {

        tel = tel.replaceAll("[^0-9]", "");

        return tel.length() == 11 && tel.charAt(2) == '9';

    }

}