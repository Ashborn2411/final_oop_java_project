package FileHandler;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.CipherOutputStream;
import javax.crypto.CipherInputStream;
import java.io.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;

public class FileHandler {
    private static final String SECRET = "IWANNASLEEPANDLOVEFOOD";
    private static final String ALGORITHM = "AES";

    private static SecretKey getKey() throws Exception {
        byte[] key = SECRET.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        return new SecretKeySpec(key, ALGORITHM);
    }

    public static void saveData(ArrayList<?> data, String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename);
             CipherOutputStream cos = new CipherOutputStream(fos, getEncryptCipher());
             ObjectOutputStream oos = new ObjectOutputStream(cos)) {
            oos.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<?> loadData(String filename) {
        try (FileInputStream fis = new FileInputStream(filename);
             CipherInputStream cis = new CipherInputStream(fis, getDecryptCipher());
             ObjectInputStream ois = new ObjectInputStream(cis)) {
            return (ArrayList<?>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private static Cipher getEncryptCipher() throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getKey());
        return cipher;
    }

    private static Cipher getDecryptCipher() throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getKey());
        return cipher;
    }
}