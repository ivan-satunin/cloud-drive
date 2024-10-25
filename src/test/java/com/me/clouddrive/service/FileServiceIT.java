package com.me.clouddrive.service;

import com.me.clouddrive.dto.view.FileView;
import com.me.clouddrive.exception.file.FileNotFoundException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FileServiceIT extends MinioIT {

    @Test
    void renameFile_ifFileExists_shouldBeRenamed() {
        var oldName = "some_pie.txt";
        var newName = "apple_pie.txt";
        fileService.renameFile(1, "recipes", oldName, newName);
        var actual = fileService.findAllUserFiles(1, "recipes").stream()
                .map(FileView::name)
                .toList();
        assertThat(actual)
                .contains(newName)
                .doesNotContain(oldName);
    }

    @Test
    void renameFile_ifFileNotExists_shouldThrow() {
        assertThatThrownBy(() -> fileService.renameFile(1, "recipes", "foo.txt", "bar.txt"))
                .isNotNull()
                .isInstanceOf(FileNotFoundException.class)
                .hasMessage("Not found file: foo.txt");
    }
}
