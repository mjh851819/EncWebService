package com.service.component.encryption;

import com.service.component.BinaryManager;
import com.service.dto.EncryptionResult;
import com.service.handler.ex.CustomCryptoException;
import com.service.handler.ex.CustomIoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static com.service.handler.ExMessage.EMPTY_FILE;
import static com.service.handler.ExMessage.ENCRYPTION_ERROR;

@Component
@RequiredArgsConstructor
public class Encryptor {

    private final BinaryManager binaryManager;

    private static String privateKey = "1a2b3c4d5e6f7g8h"; // 임의의 16bytes 비밀키 지정. 추후 보안을 위해 환경변수에 등록한 후 @Value로 사용가능.

    public EncryptionResult doEncryption(byte[] file) {
        String ivValue = UUID.randomUUID().toString().substring(0,16); // 매 암호화마다 랜덤한 IV값 생성
        Cipher c = getCipher(privateKey, ivValue, Cipher.ENCRYPT_MODE);

        try {
            return new EncryptionResult(c.doFinal(file), ivValue);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new CustomCryptoException(ENCRYPTION_ERROR);
        } catch (IllegalArgumentException e) {
            throw new CustomIoException(EMPTY_FILE);
        }
    }

    //테스트를 위한 복호화 메서드
    public byte[] doDecryption(byte[] file, String ivValue) {

        Cipher c = getCipher(privateKey, ivValue, Cipher.DECRYPT_MODE);

        try {
            return c.doFinal(file);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new CustomCryptoException(e.getMessage());
        }
    }

    public Cipher getCipher(String key, String ivValue, int modeValue) {
        SecretKeySpec secretKey = new SecretKeySpec(binaryManager.getBytecodeByString(key), "AES");
        IvParameterSpec iv = new IvParameterSpec(ivValue.getBytes());

        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(modeValue, secretKey, iv);

            return c;
        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new CustomCryptoException(e.getMessage());
        }
    }
}
