package com.service.component.encryption;

import com.service.component.BinaryManager;
import com.service.dto.EncryptionResult;
import com.service.handler.ex.CustomCryptoException;
import com.service.handler.ex.CustomIoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import java.security.SecureRandom;
import static org.assertj.core.api.Assertions.assertThat;


class EncryptorTest {

    Encryptor encryptor = new Encryptor(new BinaryManager());

    @Test
    @DisplayName("UnitTest: 암호화 기능 작동 테스트")
    public void testDoEncryptionSuccess() throws Exception {
        //given
        byte[] file = getBytes();

        //when
        EncryptionResult res = encryptor.doEncryption(file);

        //then
        assertThat(res.getFile()).isNotEmpty();
        assertThat(res.getIv()).isNotEmpty();
    }

    @Test
    @DisplayName("UnitTest: 암호화 & 복호화 검증")
    public void testEncryptionWithValidInput() throws Exception {

        //given
        byte[] file = getBytes();

        //when
        EncryptionResult res = encryptor.doEncryption(file);
        byte[] resFile = encryptor.doDecryption(res.getFile(), res.getIv());

        //then
        assertThat(file).isEqualTo(resFile);
    }

    @Test
    @DisplayName("UnitTest: 암호화 실패 테스트. 유효하지 않은 key 입력")
    public void testEncryptionWithInValidKey() throws Exception {
        //given
        String inValidKey = "1"; //잘못된 길이의 개인 키
        String iv = "1234567890123456";

        //when

        //then
        Assertions.assertThrows(CustomCryptoException.class, () -> { // InvalidKeyException 발생
            encryptor.getCipher(inValidKey, iv, Cipher.ENCRYPT_MODE);
        });
    }

    @Test
    @DisplayName("UnitTest: 암호화 실패 테스트. 유효하지 않은 iv 입력")
    public void testEncryptionWithInValidIV() throws Exception {
        //given
        String key = "1234567890123456";
        String inValidIv =  "1"; //잘못된 길이의 iv

        //when

        //then
        Assertions.assertThrows(CustomCryptoException.class, () -> { // InvalidKeyException 발생
            encryptor.getCipher(key, inValidIv, Cipher.ENCRYPT_MODE);
        });
    }

    @Test
    @DisplayName("UnitTest: 암호화 실패 테스트. 유효하지 않은 FileInput")
    public void testEncryptionWithInValidFile() throws Exception {

        //given
        byte[] file = null;

        //when
        //then
        Assertions.assertThrows(CustomIoException.class, () -> {
            encryptor.doEncryption(file);
        });
    }

    @Test
    @DisplayName("UnitTest: 복호화 실패 테스트. 잘못된 secretKey")
    public void testDecryptionWithInValidKey() throws Exception {
        //given
        byte[] file = getBytes();
        //when
        EncryptionResult res = encryptor.doEncryption(file);
        Cipher c = encryptor.getCipher("badKey1234567890", res.getIv(), Cipher.DECRYPT_MODE);

        //then
        Assertions.assertThrows(BadPaddingException.class,() -> {
            c.doFinal(res.getFile());
        });
    }

    @Test
    @DisplayName("UnitTest: 복호화 실패 테스트. 잘못된 IV")
    public void testDecryptionWithInValidIV() throws Exception {
        //given
        byte[] file = getBytes();
        //when
        EncryptionResult res = encryptor.doEncryption(file);
        byte[] resFile = encryptor.doDecryption(res.getFile(), "wrongivvalue1234");
        //then
        assertThat(file).isNotEqualTo(resFile);
    }

    private byte[] getBytes() {
        byte[] file = generateRandomBytes(100);
        return file;
    }

    public byte[] generateRandomBytes(int length) {
        byte[] bytes = new byte[length];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }
}