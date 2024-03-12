package com.service.service;

import com.service.component.FileManager;
import com.service.domain.File;
import com.service.dto.FileDto;
import com.service.repository.FileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    @Mock
    private FileManager fileManager;
    @Mock
    private FileRepository fileRepository;

    @Test
    @DisplayName("FindAll() 성공 테스트")
    public void testFindAllWithSuccess() throws Exception {
        //given
        int pageNum = 0;
        int pageSize = 5;
        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNum);

        Page<File> files = getFiles(pageable);

        //stub
        when(fileRepository.findAll(any(Pageable.class))).thenReturn(files);

        //when
        Page<File> res = fileRepository.findAll(pageable);

        //then
        assertEquals(res.getNumber(), files.getNumber());
        assertEquals(res.getSize(), files.getSize());
        assertEquals(res.getTotalElements(), files.getTotalElements());
        assertEquals(res.getTotalPages(), files.getTotalPages());
        assertEquals(res.getContent(), files.getContent());
    }

    @Test
    @DisplayName("downloadFile() 성공 테스트")
    public void testDownloadFileWithSuccess() throws Exception {

        //given
        String protocol = "file:/"; //uri 리소스를 생성하기 위해 앞에 붙여주는 프로토콜 형식
        String fileName = "abc.bin";
        String savedFileName = UUID.randomUUID().toString() + ".bin";

        //stub
        when(fileManager.getFullFilePath(any())).thenReturn("/" + savedFileName); //파일이 저장되는 루트 + 저장된 파일명이 와야하지만 테스트 편의를 위해 '/'로 설정한다.

        //when
        FileDto.DownloadFileInfo res = fileService.downloadFile(savedFileName, fileName);

        //then
        assertEquals("attachment; filename=\"" + fileName + "\"", res.getContentDisposition()); // 다운로드 될 파일의 이름을 미리 지정한다.
        assertEquals(new UrlResource(protocol + savedFileName), res.getResource()); // 프로토콜 + 저장된 파일의 위치
    }

    private Page<File> getFiles(Pageable pageable) {
        int totalElements = 7;
        List<File> files = new ArrayList<>();

        for (int i = 0; i < totalElements; i++) {
            File file = File.builder()
                    .binaryFileName("abc" + i + ".bin")
                    .savedBinaryFileName(UUID.randomUUID().toString() + ".bin")
                    .savedEncFileName(UUID.randomUUID().toString() + ".bin")
                    .iv(UUID.randomUUID().toString().substring(0,16))
                    .build();
            files.add(file);
        }

        return new PageImpl<>(files, pageable, totalElements);
    }

}