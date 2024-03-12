package com.service.dto;

import lombok.Getter;

@Getter
public class EncryptionResult {

    private byte[] file;
    private String iv;

    public EncryptionResult(byte[] file, String iv) {
        this.file = file;
        this.iv = iv;
    }
}
