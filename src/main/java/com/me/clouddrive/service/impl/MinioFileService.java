package com.me.clouddrive.service.impl;

import com.me.clouddrive.dto.Size;
import com.me.clouddrive.dto.SaveFileArgs;
import com.me.clouddrive.dto.download.DownloadFile;
import com.me.clouddrive.dto.view.FileView;
import com.me.clouddrive.exception.file.*;
import com.me.clouddrive.mapper.MinioItemMapper;
import com.me.clouddrive.utils.PathBuilder;
import com.me.clouddrive.service.FileService;
import com.me.clouddrive.service.MinioUserObjectService;
import com.me.clouddrive.utils.PathUtils;
import io.minio.GetObjectResponse;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class MinioFileService implements FileService {
    private final MinioUserObjectService userObjectService;
    private final MinioItemMapper minioItemMapper;

    @Override
    public void uploadFile(long userId, MultipartFile file, String location) {
        try {
            var uploadArgs = SaveFileArgs.builder()
                    .fileData(file.getBytes())
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .partSize(-1)
                    .build();
            userObjectService.uploadObject(uploadArgs, path -> path
                    .userFolder(userId)
                    .segment(location, file.getOriginalFilename())
                    .build()
            );
        } catch (Exception ex) {
            throw new UploadFileException(file.getOriginalFilename(), ex);
        }
    }

    @Override
    public List<FileView> findAllUserFiles(long userId, String path) {
        return userObjectService.listObjects(true, pb -> pb
                        .userFolder(userId)
                        .segment(path)
                        .build()
                )
                .map(minioItemMapper::itemToFileView)
                .toList();
    }

    @Override
    public void deleteFile(long userId, String path) {
        try {
            userObjectService.deleteObject(pb -> pb
                    .userFolder(userId)
                    .segment(path)
                    .build()
            );
        } catch (Exception ex) {
            throw new DeleteFileException(PathUtils.cutItemName(path));
        }
    }

    @Override
    public Size calcUsedSpace(long userId) {
        var usedSpace = userObjectService.listObjects(true, path -> path
                        .userFolder(userId)
                        .buildFolder()
                )
                .mapToLong(Item::size)
                .sum();
        return new Size(usedSpace);
    }

    @Override
    public void renameFile(long userId, String location, String oldName, String newName) {
        Function<PathBuilder, String> oldFileMapper = oldFile -> oldFile
                .userFolder(userId)
                .segment(location, oldName)
                .build();
        try {
            userObjectService.copyObject(
                    oldFileMapper,
                    newFile -> newFile
                            .userFolder(userId)
                            .segment(location, newName)
                            .build()
            );
            userObjectService.deleteObject(oldFileMapper);
        }
        catch (Exception ex) {
            if (ex instanceof ErrorResponseException errorRespEx) {
                var errorResp = errorRespEx.errorResponse();
                if (errorResp.code().equals("NoSuchKey"))
                    throw new FileNotFoundException(oldName);
            }
            throw new RenameFileException(oldName, ex);
        }
    }

    @Override
    public DownloadFile getDownloadFileData(long userId, String path) {
        try (GetObjectResponse resp = userObjectService.getObject(pb -> pb
                .userFolder(userId)
                .segment(path)
                .build()
        )) {
            return new DownloadFile(PathUtils.cutItemName(path), resp.readAllBytes());
        } catch (IOException e) {
            throw new DownloadFileException(PathUtils.cutItemName(path));
        }
    }
}
