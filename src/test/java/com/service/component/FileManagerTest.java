package com.service.component;

import com.service.handler.ex.CustomBadRequestException;
import com.service.handler.ex.CustomIoException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    private FileManager fileManager = new FileManager();
    private String tmp = "/encTmp/";

    @BeforeEach
    void setUp() {

        fileManager.setFilePath(tmp);
    }

    @Test
    @DisplayName("getFilePath() 기능 동작 테스트.")
    public void testGetFilePathWithSuccess() throws Exception {

        //given
        String fileName = "file.bin";

        //when
        String fullFilePath = fileManager.getFullFilePath(fileName);

        //then
        Assertions.assertThat(fullFilePath).isEqualTo(tmp + fileName);
    }

    @Test
    @DisplayName("uploadBinaryFile 성공 테스트.")
    public void testUploadBinaryFileWithSuccess() {

        //given
        byte[] file = "abcd".getBytes();
        String fileName = "abcd.bin";

        //when
        String savedFileName = fileManager.uploadBinaryFile(file, fileName);

        //then
        Path filePath = Paths.get(fileManager.getFullFilePath(savedFileName));
        File uploadedFile = filePath.toFile();
        assertTrue(uploadedFile.exists());
        assertTrue(uploadedFile.isFile());

        if (uploadedFile.exists()) {
            uploadedFile.delete(); // 파일 삭제
        }

    }

    @Test
    @DisplayName("uploadBinaryFile 실패 테스트. 빈 파일을 저장하려 할 때.")
    public void testUploadBinaryFileWithNullFile(@TempDir Path tempDir) throws Exception {

        //given
        byte[] file = null;
        String fileName = "abcd.bin";
        fileManager.setFilePath(tempDir.toString() + "\\");

        //when
        //then
        assertThrows(CustomIoException.class, () -> {
            fileManager.uploadBinaryFile(file, fileName);
        });
    }

    @Test
    @DisplayName("uploadBinaryFile 실패 테스트. 바이너리 파일 형식이 아닐때")
    public void testUploadBinaryFileWithNotBinaryFile(@TempDir Path tempDir) throws Exception {

        //given
        byte[] file = null;
        String fileName = "abcd.txt";
        fileManager.setFilePath(tempDir.toString() + "\\");

        //when
        //then
        assertThrows(CustomBadRequestException.class, () -> {
            fileManager.uploadBinaryFile(file, fileName);
        });
    }

}