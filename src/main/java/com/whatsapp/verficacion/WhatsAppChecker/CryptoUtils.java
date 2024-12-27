package com.whatsapp.verficacion.WhatsAppChecker;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class CryptoUtils {

    private static final String ALGORITHM = "AES";
    private static final Integer KEYSIZE = 256;

    public static void saveKey(SecretKey key, String fileName) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(key);
        }
    }

    public static SecretKey loadKey(String fileName) throws Exception {
            try (FileInputStream fis = new FileInputStream(fileName);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                return (SecretKey) ois.readObject();
            }
        }
    
        public static SecretKey generateKey() throws Exception {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(KEYSIZE);
            return keyGen.generateKey();
        }
    
        public static String encrypt(String data, SecretKey key) throws Exception {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedData);
        }
    
        public static String decrypt(String encryptedData, SecretKey key) throws Exception {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedData = Base64.getDecoder().decode(encryptedData);
            byte[] originalData = cipher.doFinal(decodedData);
            return new String(originalData);
        }
    
        public static void main(String[] args) {
            String cookies = "COOKIE_DATA_AQUI";
            try{
                String filename = Paths.get("rempalago_force.key").toAbsolutePath().toString();
                SecretKey secretKey = loadKey(filename);
            System.out.println(secretKey);
            String encryptedCookies = decrypt("275LR4MJl+MkYYk/bfmXsRyyAUnn1KuAK5YMiSFRWjc=", secretKey);
            System.out.println("Cookies Desemcriptadas: " + encryptedCookies);
        }catch (Exception e){
            System.out.println("Error " + e);
        } 
    }
}
