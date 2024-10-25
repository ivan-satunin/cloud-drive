package com.me.clouddrive.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PathUtilsTest {

    @ParameterizedTest
    @MethodSource("cutItemNameArgs")
    void cutItemName(String objectName, String expected) {
        var actual = PathUtils.cutItemName(objectName);

        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> cutItemNameArgs() {
        return Stream.of(
                Arguments.of("user-n-files/presentation.pptx", "presentation.pptx"),
                Arguments.of("user-n-files/folder1/sub-folder1/some.txt", "some.txt"),
                Arguments.of("folder/sub/table.xlsx", "table.xlsx"),
                Arguments.of("text.txt", "text.txt")
        );
    }
}
