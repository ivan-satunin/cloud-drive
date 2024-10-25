package com.me.clouddrive.service;

import com.me.clouddrive.config.TestConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Testcontainers
@SpringBootTest(classes = TestConfig.class)
public abstract class MinioIT {
    private static final Path PANCAKES_RECIPE_PATH = Path.of("src", "test", "resources", "files", "pancakes_recipe.txt");
    private static final Path PIE_RECIPE_PATH = Path.of("src", "test", "resources", "files", "some_pie.txt");
    private static final Path LONDON_WEATHER_JSON = Path.of("src", "test", "resources", "files", "london_weather.json");
    private static final Path TODOLIST_PATH = Path.of("src", "test", "resources", "files", "todolist.txt");

    @Autowired
    protected MinioUserObjectService minioService;

    @Autowired
    protected FileService fileService;

    @Autowired
    protected FolderService folderService;

    @BeforeEach
    void initTestObjects() throws IOException {
        fileService.uploadFile(1, toMultipartFile(PANCAKES_RECIPE_PATH, "text/plain"), "recipes");
        fileService.uploadFile(1, toMultipartFile(PIE_RECIPE_PATH, "text/plain"), "recipes");
        fileService.uploadFile(2, toMultipartFile(LONDON_WEATHER_JSON, "application/json"), MinioUserObjectService.ROOT_FOLDER);
        fileService.uploadFile(2, toMultipartFile(TODOLIST_PATH, "text/plain"), MinioUserObjectService.ROOT_FOLDER);
    }

    private MultipartFile toMultipartFile(Path path, String contentType) throws IOException {
        return new MockMultipartFile("name", path.getFileName().toString(), contentType, Files.readAllBytes(path));
    }

    @AfterEach
    void clearTestObjects() {
        folderService.deleteFolder(1);
        folderService.deleteFolder(2);
    }
}
