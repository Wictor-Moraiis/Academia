package com.wictor.Security;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import com.wictor.Security.Settings;
import static com.wictor.Security.Settings.pepper;

public class PasswordService {

    public static String Criptografia(String Senha){

        String Senha_Pepper = Senha+ pepper;

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

        int iterations = 3;
        int memory = 65536;
        int parallelism = 1;

        return argon2.hash(iterations, memory, parallelism, Senha_Pepper);
    }

    public static boolean verificarSenha(String senhaDigitada, String hashArmazenado) {
        String senhaPepper = senhaDigitada + pepper;
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

        return argon2.verify(hashArmazenado, senhaPepper);
    }



}
