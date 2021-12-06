package com.tiki_server.main;

import com.tiki_server.enums.MessageType;
import com.tiki_server.model.Message;
import com.tiki_server.util.AESUtil;

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

public class Test {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String text = "Hello world";
        SecretKey secretKey = AESUtil.generateAESKey();
        byte[] encodedKey = secretKey.getEncoded();

        byte[] encryptedContent = AESUtil.encryptFile(secretKey, text.getBytes());

        byte[] ivBytes = Arrays.copyOfRange(encryptedContent, 0, 16);
        encryptedContent = Arrays.copyOfRange(encryptedContent, 16, encryptedContent.length);
        IvParameterSpec ivParams = AESUtil.getIVParams(ivBytes);

        byte[] decrypted = AESUtil.decryptFile(secretKey, ivParams, encryptedContent);

        System.out.println("Decrypted: " + new String(decrypted));
    }
}
