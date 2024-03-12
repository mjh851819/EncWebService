package com.service.component;

import com.service.handler.ex.CustomBadRequestException;
import com.service.handler.ex.CustomIoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.UUID;

import static com.service.handler.ExMessage.BAD_REQUEST;

@Slf4j
@RequiredArgsConstructor
@Component
public class FileManager {

    @Value("${spring.servlet.multipart.location}")
    private String filePath;
    private String fileType = "bin";

    public String getFullFilePath(String fileName) {
        return filePath + fileName;
    }

    public String uploadBinaryFile(byte[] file, String fileName) {

        String savedFileName = createSavedFileName(fileName, UUID.randomUUID().toString());

        try(FileOutputStream fos = new FileOutputStream(getFullFilePath(savedFileName));) {
            fos.write(file);
            fos.close();
        } catch (NullPointerException | IOException e) {
            throw new CustomIoException(e.getMessage());
        }

        return savedFileName;
    }

    private String createSavedFileName(String fileName, String uuid) {
        String prefix = getPrefix(fileName);
        return uuid + "." + prefix;
    }

    private String getPrefix(String fileName) {
        int idx = fileName.lastIndexOf(".");
        String prefix = fileName.substring(idx + 1);
        log.info("prefix = {}", prefix);
        if(!prefix.equals(fileType)) {
            throw new CustomBadRequestException(BAD_REQUEST);
        }
        return prefix;
    }

    public void setFilePath(String path) {
        this.filePath = path;
    }

}
