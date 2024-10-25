package com.me.clouddrive.service;

import com.me.clouddrive.dto.download.ZipFolder;
import com.me.clouddrive.dto.view.FolderView;

import java.util.List;
import java.util.Set;

public interface FolderService {

    List<FolderView> findUserFolders(long userId, String path);

    default List<FolderView> findUserFolders(long userId) {
        return findUserFolders(userId, MinioUserObjectService.ROOT_FOLDER);
    }

    Set<FolderView> findAllUserFolders(long userId);

    void deleteFolder(long userId, String path);

    default void deleteFolder(long userId) {
        deleteFolder(userId, "");
    }

    void renameFolder(long userId, String location, String oldName, String newName);

    ZipFolder downloadFolder(long userId, String path);
}
