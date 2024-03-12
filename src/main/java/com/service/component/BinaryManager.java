package com.service.component;

import com.service.handler.ex.CustomIoException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Component
public class BinaryManager {

    public byte[] getBytecodeByFile(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new CustomIoException(e.getMessage());
        }
    }

    public byte[] getBytecodeByString(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new CustomIoException(e.getMessage());
        }
    }
}
