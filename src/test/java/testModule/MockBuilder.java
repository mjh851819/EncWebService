package testModule;

import org.springframework.mock.web.MockMultipartFile;

public class MockBuilder {

    static public MockMultipartFile createMultipartFile(String filename, String content) {
        return new MockMultipartFile("file", filename, "application/octet-stream", content.getBytes());
    }
}
