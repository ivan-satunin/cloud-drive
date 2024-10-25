package com.me.clouddrive.service;

import com.me.clouddrive.dto.view.StorageObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchServiceIT extends MinioIT {
    @Autowired
    private SearchService searchService;

    @ParameterizedTest
    @MethodSource("getSearchArgs")
    void search(long userId, String query, List<String> expectedResultNames) {
        var actualResults = searchService.search(userId, query);
        var actualResultNames = actualResults.stream()
                .map(StorageObject::name).toList();
        assertThat(actualResultNames).isEqualTo(expectedResultNames);
    }

    static Stream<Arguments> getSearchArgs() {
        return Stream.of(
                Arguments.of(1L, "pie", List.of("some_pie.txt")),
                Arguments.of(2L, "txt", List.of("todolist.txt"))
        );
    }
}
