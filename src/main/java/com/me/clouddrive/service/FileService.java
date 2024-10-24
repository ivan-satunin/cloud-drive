package com.me.clouddrive.service;

import com.me.clouddrive.dto.Size;
import com.me.clouddrive.dto.download.DownloadFile;
import com.me.clouddrive.dto.view.FileView;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    void uploadFile(long userId, MultipartFile file, String location);

    List<FileView> findAllUserFiles(long userId, String path);

    default List<FileView> findAllUserFiles(long userId) {
        return findAllUserFiles(userId, MinioUserObjectService.ROOT_FOLDER);
    }

    void deleteFile(long userId, String path);

    Size calcUsedSpace(long userId);

    void renameFile(long userId, String location, String oldName, String newName);

    DownloadFile getDownloadFileData(long userId, String path);
}
