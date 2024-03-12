package com.service.controller;

import com.service.component.FileManager;
import com.service.domain.File;
import com.service.dto.FileDto;
import com.service.repository.FileRepository;
import com.service.service.EncService;
import com.service.service.FileService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import testModule.MockBuilder;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.service.dto.FileDto.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional //테스트 이후 트랜잭션 롤백
@AutoConfigureMockMvc //MockMvc 자동 주입 & 구성하여 http 요청 테스트 가능
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // 실제 서블릿 컨테이너를 올리지 않고 모의 환경에서 테스트 가능하게 해주는 어노테이션
class EncControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private FileRepository fileRepository;

    @BeforeEach
    public void setUp(){
        for (int i = 0; i < 7; i++) {
            File file = File.builder()
                    .binaryFileName("abc" + i + ".bin")
                    .savedBinaryFileName(UUID.randomUUID() + ".bin")
                    .savedEncFileName(UUID.randomUUID() + ".bin")
                    .iv(UUID.randomUUID().toString().substring(0,16))
                    .build();


            fileRepository.save(file);
        }
    }

    @Test
    @DisplayName("fileHome 동작 테스트. 매개변수 x")
    public void testFileHomeWithoutPara() throws Exception {

        //given

        //when
        ResultActions resultAction = mvc.perform(get("/file"));

        //then
        resultAction
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    @DisplayName("fileHome 페이징 기능 검증. 매개변수 o")
    public void testFileHomeWithPaging() throws Exception {

        //given

        //when
        ResultActions resultAction = mvc.perform(get("/file").param("page", "1").param("size", "5"));
        Map<String, Object> model = resultAction.andReturn().getModelAndView().getModel();
        List<FileInfo> files = (List<FileInfo>) model.get("files");
        Integer currentPage = (Integer) model.get("currentPage");
        Integer totalPage = (Integer) model.get("totalPage");

        System.out.println("files : " + files);
        System.out.println("currentPage : " + currentPage);
        System.out.println("totalPage : " + totalPage);

        //then
        Assertions.assertThat(files.size()).isEqualTo(5);
        Assertions.assertThat(currentPage).isEqualTo(1);
        Assertions.assertThat(totalPage).isEqualTo(2);
    }

    @Test
    @DisplayName("upload 기능 테스트")
    public void testUploadWithSuccess() throws Exception {
        //given
        String fileName = "abc.bin";
        String fileContents = "abc";
        MockMultipartFile multipartFile = MockBuilder.createMultipartFile(fileName, fileContents);

        //when
        ResultActions resultAction = mvc.perform(multipart("/file").file(multipartFile));

        //then
        resultAction
                .andExpect(status().isOk())
                .andExpect(view().name("home"));

        Assertions.assertThat(fileRepository.count()).isEqualTo(8);
    }

    @Test
    @DisplayName("downloadFile 기능 테스트")
    public void testDownloadFileWithSuccess() throws Exception {

        //given
        String fileName = "test.bin";
        String savedFileName = "test.bin";

        //when
        ResultActions resultAction = mvc.perform(get("/download/{savedFileName}", savedFileName).param("fileName", fileName));

        //then
        resultAction
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\""));
    }

}