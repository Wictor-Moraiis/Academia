package com.wictor.Security;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.wictor.Security.Settings.getKey;

public class CpfService {
    public static String Criptografia(String Cpf) {

        try {

            Cipher cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.ENCRYPT_MODE, getKey());

            byte[] Cpf_cript = cipher.doFinal(Cpf.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(Cpf_cript);

        } catch (Exception e) {

            throw new RuntimeException(e);
        }

    }

    public static String Descriptografia(String Cpf_cripto) {

        try {

            Cipher cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.DECRYPT_MODE, getKey());

            byte[] decoded = Base64.getDecoder().decode(Cpf_cripto);

            byte[] Cpf = cipher.doFinal(decoded);

            return new String(Cpf, StandardCharsets.UTF_8);

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

}

