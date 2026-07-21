package com.wictor.audit;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Component
public class ComparadorAuditoria {


    public String comparar(Object antes, Object depois) {

        if (antes == null) {

            return "Registro criado: " + depois.getClass().getSimpleName();
        }

        if (depois == null) {

            return "Registro removido: " + antes.getClass().getSimpleName();
        }

        List<String> alteracoes = new ArrayList<>();

        try {

            for (Field campo : antes.getClass().getDeclaredFields()) {

                campo.setAccessible(true);

                Object antigo = campo.get(antes);
                Object novo = campo.get(depois);

                if (antigo == null && novo == null) {continue;}

                if (antigo == null || !antigo.equals(novo)) {

                    alteracoes.add(campo.getName() + ": " + antigo + " → " + novo);
                }
            }

        } catch (IllegalAccessException e) {

            throw new RuntimeException(e);
        }

        return String.join("; ", alteracoes);
    }
}