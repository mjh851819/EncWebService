package com.service.service;

import com.service.component.BinaryManager;
import com.service.component.FileManager;
import com.service.component.encryption.Encryptor;
import com.service.domain.File;
import com.service.dto.EncryptionResult;
import com.service.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class EncService {

    private final FileManager fileManager;
    private final Encryptor encryptor;
    private final BinaryManager binaryManager;
    private final FileRepository fileRepository;

    @Transactional
    public void encryptionFile(MultipartFile file) {

        byte[] targetFile = binaryManager.getBytecodeByFile(file);

        String fileName = file.getOriginalFilename();
        String savedFileName = fileManager.uploadBinaryFile(targetFile, fileName);
        EncryptionResult res = encryptor.doEncryption(targetFile);
        String savedEncFileName = fileManager.uploadBinaryFile(res.getFile(), fileName);

        File entity = File.builder()
                .binaryFileName(fileName)
                .savedBinaryFileName(savedFileName)
                .savedEncFileName(savedEncFileName)
                .iv(res.getIv())
                .build();

        File PSFile = fileRepository.save(entity);
    }

}
