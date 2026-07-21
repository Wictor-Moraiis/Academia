package com.wictor.util;

public class NumberValidator {

        public static String limpar(String tel) {

            if (tel == null) return null;
            return tel.replaceAll("\\D", "");
        }
}