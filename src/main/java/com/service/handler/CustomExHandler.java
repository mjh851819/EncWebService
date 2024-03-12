package com.service.handler;

import com.service.handler.ex.CustomCryptoException;
import com.service.handler.ex.CustomDBException;
import com.service.handler.ex.CustomIoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class CustomExHandler {

    @ExceptionHandler(CustomCryptoException.class)
    public ResponseEntity<?> EncryptionException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("암호화 관련 작업중 오류가 발생했습니다. 오류 정보: " + ex.getMessage());
    }

    @ExceptionHandler(CustomIoException.class)
    public ResponseEntity<?> IoException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("파일 관련 작업중 오류가 발생했습니다. " + ex.getMessage());
    }

    @ExceptionHandler(CustomDBException.class)
    public ResponseEntity<?> DBException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("DB 작업중 오류가 발생했습니다. " + ex.getMessage());
    }


}
