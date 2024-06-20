package com.lsx.finalhomework.entities;

import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    // 示例密钥和IV，实际应用中应从安全的地方获取或生成
    private static final String KEY = "1234567890123456"; // 密钥必须是16、24或32字节长
    private static final String IV = "1234567890123456"; // 16字节长的IV

    //加密
    public static String encrypt(String value) throws Exception {
        SecretKey secretKey = generateKey(KEY);
        IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes("UTF-8"));

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        byte[] encryptedBytes = cipher.doFinal(value.getBytes("UTF-8"));
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
    }

    // 解密
    public static String decrypt(String encryptedValue) throws Exception {
        SecretKey secretKey = generateKey(KEY);
        IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes("UTF-8"));

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        byte[] decodedBytes = Base64.decode(encryptedValue, Base64.DEFAULT);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, "UTF-8");
    }

    // 生成密钥
    private static SecretKey generateKey(String password) throws UnsupportedEncodingException {
        // 实际应用中可能需要使用PBKDF2等更安全的方式来生成密钥
        byte[] keyBytes = password.getBytes("UTF-8");
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);
        return keySpec;
    }
}