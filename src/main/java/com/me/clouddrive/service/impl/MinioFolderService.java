package com.me.clouddrive.service.impl;

import com.me.clouddrive.dto.download.DownloadFile;
import com.me.clouddrive.dto.download.ZipFolder;
import com.me.clouddrive.dto.view.FolderView;
import com.me.clouddrive.exception.file.DownloadFileException;
import com.me.clouddrive.exception.folder.FolderNotFoundException;
import com.me.clouddrive.exception.folder.RenameFolderException;
import com.me.clouddrive.mapper.MinioItemMapper;
import com.me.clouddrive.utils.PathBuilder;
import com.me.clouddrive.service.FolderService;
import com.me.clouddrive.service.MinioUserObjectService;
import com.me.clouddrive.utils.PathUtils;
import io.minio.GetObjectResponse;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MinioFolderService implements FolderService {
    private final MinioUserObjectService minioService;
    private final MinioItemMapper minioItemMapper;

    @Override
    public List<FolderView> findUserFolders(long userId, String path) {
        return minioService.listObjects(pb -> pb
                        .userFolder(userId)
                        .segment(path)
                        .buildFolder()
                )
                .filter(Item::isDir)
                .map(minioItemMapper::itemToFolderView)
                .toList();
    }

    @Override
    public Set<FolderView> findAllUserFolders(long userId) {
        return minioService.listObjects(true, path -> path
                        .userFolder(userId)
                        .buildFolder()
                )
                .map(PathUtils::cutLocation)
                .flatMap(this::splitFolders)
                .collect(Collectors.toSet());
    }

    private Stream<FolderView> splitFolders(String location) {
        List<FolderView> folderViews = new ArrayList<>();
        var breadCrumbs = List.of(location.split("/"));
        for (var i = 0; i < breadCrumbs.size(); i++) {
            var folderName = breadCrumbs.get(i);
            if (folderName.isEmpty())
                continue;
            var folderLocation = String.join("/", breadCrumbs.subList(0, i));
            folderViews.add(new FolderView(folderName, folderLocation));
        }
        return folderViews.stream();
    }

    @Override
    public void deleteFolder(long userId, String path) {
        var deleteObjects = minioService.listObjects(true, pb -> pb
                        .userFolder(userId)
                        .segment(path)
                        .buildFolder()
                )
                .map(item -> new DeleteObject(item.objectName()))
                .toList();
        minioService.deleteObjects(deleteObjects);
    }

    @Override
    public void renameFolder(long userId, String location, String oldName, String newName) {
        var oldFolderPath = new PathBuilder()
                .segment(location, oldName)
                .build();
        var folderItems = minioService.listObjects(path -> path
                .userFolder(userId)
                .segment(oldFolderPath)
                .buildFolder()
        ).toList();
        if (folderItems.isEmpty()) {
            throw new FolderNotFoundException(oldName);
        }
        try {
            folderItems.forEach(item -> minioService.copyObject(
                    oldPath -> oldPath
                            .userFolder(userId)
                            .segment(oldFolderPath, PathUtils.cutItemName(item))
                            .build(),
                    newPath -> newPath
                            .userFolder(userId)
                            .segment(location, newName, PathUtils.cutItemName(item))
                            .build()
            ));
            deleteFolder(userId, oldFolderPath);
        } catch (Exception ex) {
            throw new RenameFolderException(oldName, ex);
        }
    }

    @Override
    public ZipFolder downloadFolder(long userId, String path) {
        var downloadFiles = minioService.listObjects(true, pb -> pb
                        .userFolder(userId)
                        .segment(path)
                        .buildFolder()
                )
                .map(item -> minioService.getObject(item.objectName()))
                .map(resp -> new DownloadFile(PathUtils.cutItemName(resp.object()), readFileData(resp)))
                .toList();
        return new ZipFolder(PathUtils.cutItemName(path), downloadFiles);
    }

    private byte[] readFileData(GetObjectResponse resp) {
        try {
            return resp.readAllBytes();
        } catch (IOException e) {
            throw new DownloadFileException(PathUtils.cutItemName(resp.object()));
        }
    }
}
