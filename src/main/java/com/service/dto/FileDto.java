package com.service.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.Resource;

import java.time.LocalDateTime;
import java.util.List;

public class FileDto {

    @Data
    public static class FileInfo {
        public Long id;
        public String fileName;
        public String encFileName;
        public String savedFileName;
        public String savedEncFileName;
        public String iv;
        public LocalDateTime createdAt;

        @Builder
        public FileInfo(com.service.domain.File file) {
            this.id = file.getId();
            this.fileName = file.getBinaryFileName();
            this.savedFileName = file.getSavedBinaryFileName();
            this.savedEncFileName = file.getSavedEncFileName();
            this.iv = file.getIv();
            this.createdAt = file.getCreatedAt();

            int idx = fileName.lastIndexOf(".");
            this.encFileName = file.getBinaryFileName().substring(0, idx) + "_enc." + fileName.substring(idx + 1);
        }
    }

    @Data
    public static class Files {
        public int currentPageNum;
        public int totalPageNum;
        public List<FileInfo> fileInfos;

        public Files(int currentPageNum, int totalPageNum, List<FileInfo> fileInfos) {
            this.currentPageNum = currentPageNum;
            this.totalPageNum = totalPageNum;
            this.fileInfos = fileInfos;
        }
    }

    @Data
    public static class DownloadFileInfo {
        public String contentDisposition;
        public Resource resource;

        public DownloadFileInfo(String contentDisposition, Resource resource) {
            this.contentDisposition = contentDisposition;
            this.resource = resource;
        }
    }
}
