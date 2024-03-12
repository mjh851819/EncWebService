package com.service.component;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import testModule.MockBuilder;

class BinaryManagerTest {

    private BinaryManager binaryManager = new BinaryManager();

    @Test
    @DisplayName("File 클래스의 getByte() 성공 테스트.")
    public void testGetBytecodeByFileWithSuccess() throws Exception {

        //given
        MockMultipartFile file = MockBuilder.createMultipartFile("test.bin", "abcd");

        //when
        byte[] res = binaryManager.getBytecodeByFile(file);

        //then
        Assertions.assertThat(res).isNotEmpty();
    }

    @Test
    @DisplayName("File 클래스의 getByte() 성공 테스트.")
    public void testGetBytecodeByStringWithSuccess() throws Exception {

        //given
        String str = "abcd";

        //when
        byte[] res = binaryManager.getBytecodeByString(str);

        //then
        Assertions.assertThat(res).isNotEmpty();
    }

}