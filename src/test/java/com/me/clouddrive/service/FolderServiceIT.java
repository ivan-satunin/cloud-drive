package com.me.clouddrive.service;

import com.me.clouddrive.dto.view.FolderView;
import com.me.clouddrive.exception.folder.FolderNotFoundException;
import org.junit.jupiter.api.Test;

import static com.me.clouddrive.service.MinioUserObjectService.ROOT_FOLDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class FolderServiceIT extends MinioIT {

    @Test
    void renameFolder_ifFolderExists_shouldBeRenamedAndNotContainsDuplicate() {
        var oldName = "recipes";
        var newName = "my-recipes";
        folderService.renameFolder(1, ROOT_FOLDER, oldName, newName);
        var actualFolderNames = folderService.findUserFolders(1).stream().map(FolderView::name)
                .toList();
        assertThat(actualFolderNames)
                .isNotEmpty()
                .contains(newName)
                .doesNotContain(oldName);
    }

    @Test
    void renameFolder_ifFolderNotExists_shouldThrowNotFound() {
        var throwable = catchThrowable(() -> folderService.renameFolder(1, ROOT_FOLDER, "foo", "bar"));
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(FolderNotFoundException.class)
                .hasMessage("Not found folder: foo");
    }
}
