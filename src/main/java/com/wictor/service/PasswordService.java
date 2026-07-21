package com.wictor.service;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import static com.wictor.security.Settings.pepper;

public class PasswordService {

    public static String Criptografia(String senha) {

        char[] senhaPepper = (senha + pepper).toCharArray();

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

        try {
            return argon2.hash(3, 65536, 1, senhaPepper);
        } finally {
            argon2.wipeArray(senhaPepper);
        }
    }

    public static boolean verificarSenha(String senhaDigitada, String hashArmazenado) {

        char[] senhaPepper = (senhaDigitada + pepper).toCharArray();

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

        try {
            return argon2.verify(hashArmazenado, senhaPepper);
        } finally {
            argon2.wipeArray(senhaPepper);
        }
    }
}
