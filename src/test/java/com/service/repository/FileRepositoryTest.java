package com.service.repository;

import com.service.domain.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FileRepositoryTest {

    @Autowired
    private FileRepository fileRepository;

    private int totalSize = 7;


    @BeforeEach
    public void Setup() {
        for (int i = 0; i < totalSize; i++) {
            File file = File.builder()
                    .binaryFileName("abc" + i + ".bin")
                    .savedBinaryFileName(UUID.randomUUID().toString() + ".bin")
                    .savedEncFileName(UUID.randomUUID().toString() + ".bin")
                    .iv(UUID.randomUUID().toString().substring(0,16))
                    .build();

            fileRepository.save(file);
        }
    }

    @Test
    @DisplayName("FindAll() 성공 테스트.")
    public void testFindAllWithSuccess() throws Exception {

        //given
        int pageNumber = 0;
        int pageSize = 5;
        int totalElements = totalSize;
        int totalPage = totalSize / pageSize + (totalSize % pageSize == 0 ? 0 : 1);

        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);

        //when
        Page<File> res = fileRepository.findAll(pageable);

        //then
        assertEquals(res.getNumber(), pageNumber);
        assertEquals(res.getSize(), pageSize);
        assertEquals(res.getTotalElements(), totalElements);
        assertEquals(res.getTotalPages(), totalPage);
    }
}