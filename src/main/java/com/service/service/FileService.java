package com.service.service;

import com.service.component.FileManager;
import com.service.domain.File;
import com.service.handler.ex.CustomIoException;
import com.service.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static com.service.dto.FileDto.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FileService {

    private final FileRepository fileRepository;
    private final FileManager fileManager;

    public Files findAll(Pageable pageable) {
        Page<File> res = fileRepository.findAll(pageable);

        List<FileInfo> collect = res.stream().map(m ->
                new FileInfo(m)
        ).collect(Collectors.toList());

        return new Files(res.getNumber(),res.getTotalPages(),collect);
    }

    public DownloadFileInfo downloadFile(String savedFileName, String fileName) {
        UrlResource resourcePath = getUrlResource(savedFileName);

        String encodedFileName = UriUtils.encode(fileName,
                StandardCharsets.UTF_8);
        String downloadFileNameSetting = "attachment; filename=\"" +
                encodedFileName + "\"";

        return new DownloadFileInfo(downloadFileNameSetting, resourcePath);
    }

    private UrlResource getUrlResource(String savedFileName)  {
        try {
            UrlResource resource = new UrlResource("file:" +
                    fileManager.getFullFilePath(savedFileName));
            return resource;
        } catch (MalformedURLException e) {
            throw new CustomIoException(e.getMessage());
        }
    }
}
