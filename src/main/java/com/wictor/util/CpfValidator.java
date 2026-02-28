package com.wictor.util;

public class CpfValidator {

    public static boolean validar(String cpf) {

        int peso1 = 10;
        int peso2 = 11;
        int soma1 = 0;
        int soma2 = 0;
        int digito1 = 0;
        int digito2 = 0;

        cpf = cpf.replaceAll("[^0-9]", "");
        int[] digitos = new int[11];

        if (cpf.length() != 11){
            return false;
        }

        for (int i = 0; i < 11; i++) {

            digitos[i] = cpf.charAt(i) - '0';
        }

        if (cpf.equals("00000000000") || cpf.equals("11111111111")
                || cpf.equals("22222222222") || cpf.equals("33333333333")
                || cpf.equals("44444444444") || cpf.equals("55555555555")
                || cpf.equals("66666666666") || cpf.equals("77777777777")
                || cpf.equals("88888888888") || cpf.equals("99999999999")) return false;

        for (int i = 0; i < 9; i++) {

            soma1 += digitos[i] * peso1;
            peso1--;
        }

        for (int i = 0; i < 10; i++) {

            soma2 += digitos[i] * peso2;
            peso2--;
        }

        digito1 = (soma1 * 10) % 11;
        digito2 = (soma2 * 10) % 11;

        if (digito1 == 10) {
            digito1 = 0;
        }

        if (digito2 == 10) {
            digito2 = 0;
        }

        return digito1 == digitos[9] && digito2 == digitos[10];


    }

}
