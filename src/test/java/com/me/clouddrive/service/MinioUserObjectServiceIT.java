package com.me.clouddrive.service;

import com.me.clouddrive.dto.SaveFileArgs;
import com.me.clouddrive.path.PathBuilder;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class MinioUserObjectServiceIT extends MinioIT {
    private static final String MINIO_OBJECT_NOT_FOUND_CODE = "NoSuchKey";

    private static final String PANCAKES_RECIPE_OBJECT_NAME = new PathBuilder()
            .userFolder(1)
            .segment("recipes")
            .segment("pancakes_recipe.txt")
            .build();
    private static final String SOME_PIE_OBJECT_NAME = new PathBuilder()
            .userFolder(1)
            .segment("recipes")
            .segment("some_pie.txt")
            .build();
    private static final String LONDON_WEATHER_OBJECT_NAME = new PathBuilder()
            .userFolder(2)
            .segment("weather")
            .segment("london_weather.json")
            .build();// TODO why?

    @Value("${minio.user.bucket}")
    private String userBucket;

    @Test
    void uploadObject_shouldBeSaved() throws IOException {
        var pancakesRecipePath = Path.of("src", "test", "resources", "files", "salad.txt");
        var expectedFileData = Files.readAllBytes(pancakesRecipePath);
        var size = Files.size(pancakesRecipePath);
        var saveArgs = SaveFileArgs.builder()
                .contentType("text/plain")
                .fileData(expectedFileData)
                .size(size)
                .partSize(-1)
                .build();
        minioService.uploadObject(saveArgs, path -> path
                .userFolder(1)
                .segment("recipes", "salad")
                .segment("salad.txt")
                .build());
        var actual = minioService.getObject(path -> path
                .userFolder(1)
                .segment("recipes", "salad")
                .segment("salad.txt")
                .build());
        assertThat(actual.bucket()).isEqualTo("user-files");
        assertThat(actual.readAllBytes()).isEqualTo(expectedFileData);
    }

    @Test
    void deleteObject_shouldBeDeleted() {
        Function<PathBuilder, String> objectPath = path -> path
                .userFolder(1)
                .segment("recipes", "pancakes")
                .segment("simple_pancake.txt")
                .build();
        minioService.deleteObject(objectPath);
        var throwable = catchThrowable(() -> minioService.getObject(objectPath));
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(ErrorResponseException.class);
        var errorRespException = (ErrorResponseException) throwable;
        var errorResp = errorRespException.errorResponse();
        assertThat(errorResp.code()).isEqualTo(MINIO_OBJECT_NOT_FOUND_CODE);
    }

    @Test
    void copyObject_shouldBeCopied() throws IOException {
        Function<PathBuilder, String> fromPathFunc = fromPath -> fromPath
                .userFolder(1)
                .segment("recipes")
                .segment("pancakes_recipe.txt")
                .build();
        Function<PathBuilder, String> toPathFunc = toPath -> toPath
                .userFolder(1)
                .segment("favourite-recipes", "pancakes")
                .segment("favourite-1.txt")
                .build();
        minioService.copyObject(fromPathFunc, toPathFunc);
        var source = minioService.getObject(fromPathFunc);
        var copied = minioService.getObject(toPathFunc);
        assertThat(copied.readAllBytes())
                .isEqualTo(source.readAllBytes());
    }

    @Test
    void getObject_ifFileUploaded_shouldBe() throws IOException {
        var actual = minioService.getObject(path -> path
                .userFolder(1)
                .segment("recipes")
                .segment("pancakes_recipe.txt")
                .build());
        assertThat(actual.bucket()).isEqualTo(userBucket);
        assertThat(actual.readAllBytes())
                .isNotNull();
    }

    @Test
    void getObject_ifFileNotUploaded_shouldBeThrow() {
        var throwable = catchThrowable(() -> minioService.getObject(path -> path
                .userFolder(1)
                .segment("recipes", "pies")
                .segment("apple_pie.txt")
                .build()));
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(ErrorResponseException.class);
        var errorRespException = (ErrorResponseException) throwable;
        var errorResp = errorRespException.errorResponse();
        assertThat(errorResp.code()).isEqualTo(MINIO_OBJECT_NOT_FOUND_CODE);
    }

    @Test
    void listObjects_shouldBe() {
        var actual = minioService.listObjects(true, path -> path
                .userFolder(1)
                .build()).toList();
        var actualObjectNames = actual.stream().map(Item::objectName).toList();
        assertThat(actualObjectNames)
                .isEqualTo(List.of(PANCAKES_RECIPE_OBJECT_NAME, SOME_PIE_OBJECT_NAME));
    }

    @Test
    void deleteObjects_shouldBeDeleted() {
        var deleteObjects = List.of(
                new DeleteObject(new PathBuilder()
                        .userFolder(1)
                        .segment("recipes")
                        .segment("pancakes_recipe.txt")
                        .build()),
                new DeleteObject(new PathBuilder()
                        .userFolder(1)
                        .segment("recipes")
                        .segment("some_pie.txt")
                        .build())
        );
        minioService.deleteObjects(deleteObjects);
        var actual = minioService.listObjects(true, path -> path
                .userFolder(1)
                .segment("recipes")
                .buildFolder());
        assertThat(actual).isEmpty();
    }
}
