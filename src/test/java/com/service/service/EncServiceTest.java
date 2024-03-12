package com.service.service;

import com.service.component.BinaryManager;
import com.service.component.FileManager;
import com.service.component.encryption.Encryptor;
import com.service.domain.File;
import com.service.dto.EncryptionResult;
import com.service.repository.FileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import testModule.MockBuilder;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EncServiceTest {

    @InjectMocks
    private EncService encService;

    @Mock
    private FileManager fileManager;
    @Mock
    private Encryptor encryptor;
    @Mock
    private BinaryManager binaryManager;
    @Mock
    private FileRepository fileRepository;

    @Test
    @DisplayName("encryptionFile() 성공 테스트")
    public void testEncryptionFileWithSuccess() throws Exception {

        //given
        String fileName = "abc.bin";
        String fileContent = "abc";
        String savedFileName = UUID.randomUUID() + ".bin";
        String iv = UUID.randomUUID().toString().substring(0,16);
        MockMultipartFile multipartFile = MockBuilder.createMultipartFile(fileName, fileContent);
        EncryptionResult dto = new EncryptionResult(multipartFile.getBytes(), iv);
        File fileEntity = File.builder()
                .binaryFileName(fileName)
                .savedBinaryFileName(savedFileName)
                .savedEncFileName(savedFileName)
                .iv(iv)
                .build();

        //stub
        when(binaryManager.getBytecodeByFile(any())).thenReturn(multipartFile.getBytes());
        when(fileManager.uploadBinaryFile(any(), any())).thenReturn(savedFileName);
        when(encryptor.doEncryption(any())).thenReturn(dto);
        when(fileRepository.save(any())).thenReturn(fileEntity);

        //when
        //then
        encService.encryptionFile(multipartFile);
    }

}