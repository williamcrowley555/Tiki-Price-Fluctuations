package com.tiki_server.main;

import com.tiki_server.util.AESUtil;
import com.tiki_server.util.BytesUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class AESTest {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String text = "Hello world";
        System.out.println("Plain text: " + text);

        byte[] encodeText = BytesUtil.decode(text);

        // Encrypt
        SecretKey secretKey = AESUtil.generateAESKey();
        byte[] encryptedContent = AESUtil.encrypt(secretKey, encodeText);

        // Decrypt
        byte[] ivBytes = Arrays.copyOfRange(encryptedContent, 0, 16);
        byte[] contentBytes = Arrays.copyOfRange(encryptedContent, 16, encryptedContent.length);

        IvParameterSpec ivParams = AESUtil.getIVParams(ivBytes);
        System.out.println("IV: " + Base64.getEncoder().encodeToString(ivBytes));

        byte[] decrypted = AESUtil.decrypt(secretKey, ivParams, contentBytes);

        System.out.println("Decrypted: " + BytesUtil.encode(decrypted));
    }
}
