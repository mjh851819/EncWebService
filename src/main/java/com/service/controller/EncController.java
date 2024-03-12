package com.service.controller;

import com.service.service.EncService;
import com.service.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.service.dto.FileDto.*;

@RequiredArgsConstructor
@Controller
public class EncController {

    private final EncService encService;
    private final FileService fileService;

    @GetMapping("/file")
    public String fileHome(@RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "5") int size,
                           Model model) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending()); // 사용자에게 직관적인 번호를 받아오기 위한 라인
        Files filePage = fileService.findAll(pageable);

        model.addAttribute("files", filePage.getFileInfos());
        model.addAttribute("currentPage", filePage.getCurrentPageNum() + 1); // 페이지 번호를 1부터 시작하도록 변경
        model.addAttribute("totalPage", filePage.getTotalPageNum());

        return "home";
    }

    @PostMapping("/file")
    public String upload(@RequestParam MultipartFile file) {
        encService.encryptionFile(file);

        return "home";
    }

    @GetMapping("/download/{savedFileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String savedFileName, @RequestParam String fileName) {
        DownloadFileInfo res = fileService.downloadFile(savedFileName, fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, res.getContentDisposition())
                .body(res.getResource());
    }
}
