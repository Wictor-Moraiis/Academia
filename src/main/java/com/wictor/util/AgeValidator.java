package com.wictor.util;

import java.time.LocalDate;

public class AgeValidator {

    public static boolean validar(LocalDate datanasc) {

        LocalDate hoje = LocalDate.now();

        if (datanasc.isAfter(hoje)) return false;

        if (datanasc.isAfter(hoje.minusYears(10)))return false;

        if (datanasc.isBefore(hoje.minusYears(130)))return false;

        return true;

    }

}