package com.growfin.supportserv.utils;

import com.growfin.supportserv.exceptions.CryptException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class CryptUtils {

    private static String KEY;

    private static final String ENCRYPT_ALGORITHM = "AES";

    private static final String DECRYPT_ALGORITHM = "AES";

    private static final String ENC_DEC_ERROR_MSG="Invalid resource Id";

    @Value("${serv.encrypt.decrypt.key:null}")
    public void setKEY(String key){
        CryptUtils.KEY = key;
    }

    public static String encryptData(String encryptStr) {
        return encryptDataWithKey(encryptStr, KEY);
    }

    public static String encryptDataWithKey(String encryptStr, String key) {
        try {
            if(StringUtils.isNotEmpty(encryptStr)) {
                Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
                SecretKeySpec keySpec=new SecretKeySpec(key.getBytes(), ENCRYPT_ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, keySpec);
                byte[] cipherBytes = cipher.doFinal(encryptStr.getBytes());
                return Base64.encodeBase64URLSafeString(cipherBytes);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new CryptException(ENC_DEC_ERROR_MSG,e);
        }
        return null;
    }

    public static String encryptLongData(Long encryptValue) {
        return Optional.ofNullable(encryptValue).map(encryptStr->encryptData(String.valueOf(encryptStr))).orElse(null);
    }

    public static String encryptIntegerData(Integer encryptValue) {
        return Optional.ofNullable(encryptValue).map(encryptStr->encryptData(String.valueOf(encryptStr))).orElse(null);
    }


    public static String decryptData(String dataBytes) {
        return decryptDataWithKey(dataBytes, KEY);
    }

    public static String decryptDataWithKey(String dataBytes, String key) {
        try {
            if(StringUtils.isNotEmpty(dataBytes)) {
                Cipher cipher=Cipher.getInstance(DECRYPT_ALGORITHM);
                SecretKeySpec keySpec=new SecretKeySpec(key.getBytes(), DECRYPT_ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, keySpec);
                byte[] decodeBase64 = Base64.decodeBase64(dataBytes);
                byte[] cipherBytes = cipher.doFinal(decodeBase64);
                String decData = new String(cipherBytes);
                if(StringUtils.isNotEmpty(decData)){
                    return decData;
                }
                return null;
            }
        }  catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            return null;
        }
        return dataBytes;

    }

    public static Long decryptLongData(String dataBytes) {
        String decryptData = decryptData(dataBytes);
        if(decryptData!=null)
            return Long.parseLong(decryptData);
        return null;
    }

}
