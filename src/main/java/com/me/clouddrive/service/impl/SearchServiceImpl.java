package com.me.clouddrive.service.impl;

import com.me.clouddrive.dto.view.StorageObject;
import com.me.clouddrive.service.FileService;
import com.me.clouddrive.service.FolderService;
import com.me.clouddrive.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
@Service
public class SearchServiceImpl implements SearchService {
    private final FileService minioFileService2;
    private final FolderService minioFolderService2;

    @Override
    public List<StorageObject> search(long userId, String query) {
        var fileResults = minioFileService2.findAllUserFiles(userId).stream()
                .filter(fileView -> fileView.name().contains(query))
                .toList();
        var folderResults = minioFolderService2.findAllUserFolders(userId).stream()
                .filter(folderView -> folderView.path().contains(query))
                .collect(toSet());
        List<StorageObject> searchResults = new ArrayList<>(fileResults);
        searchResults.addAll(folderResults);
        return searchResults;
    }
}
