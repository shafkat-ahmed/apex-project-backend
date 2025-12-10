package com.apex.template.common.util;

import com.apex.template.common.exception.NullPasswordException;
import com.apex.template.common.security.SymfonySha512PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {

    public static int PASS_MIN_SIZE = 6;
    public static final int CRYPTO_STRENGTH = 512;

    public enum EncType {SHA_ENCODER, BCRYPT_ENCODER}

    public static PasswordEncoder getBCryptPasswordEncoder() {
        return org.springframework.security.crypto.factory
                .PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public static PasswordEncoder getPasswordEncoder() {
        return getBCryptPasswordEncoder();
    }

    public static String encryptPassword(String password) throws Exception {

        if (password == null)
            throw new NullPasswordException("Password can not be empty!");

        return getPasswordEncoder().encode(password);
    }

    public static String encryptPassword(String password, EncType encryptType, String salt) throws Exception {
        if (password == null) throw new NullPasswordException("Password can not be empty!");
        if (encryptType.equals(EncType.SHA_ENCODER)) {
            if (salt != null)
                return getShaPasswordEncoder().encode(password, salt);
            else getShaPasswordEncoder().encode(password);
        } else if (encryptType.equals(EncType.BCRYPT_ENCODER))
            return getBCryptPasswordEncoder().encode(password);
        return getBCryptPasswordEncoder().encode(password);
    }

    public static SymfonySha512PasswordEncoder getShaPasswordEncoder() {
        return new SymfonySha512PasswordEncoder();
    }
}