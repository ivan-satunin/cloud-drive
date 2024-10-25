package com.me.clouddrive.mapper.impl;

import com.me.clouddrive.dto.Size;
import com.me.clouddrive.dto.view.FileView;
import com.me.clouddrive.dto.view.FolderView;
import com.me.clouddrive.mapper.MinioItemMapper;
import com.me.clouddrive.utils.PathUtils;
import io.minio.messages.Item;

import java.time.ZoneId;

public class MinioItemMapperImpl implements MinioItemMapper {

    @Override
    public FileView itemToFileView(Item item) {
        return FileView.builder()
                .name(PathUtils.cutItemName(item))
                .size(new Size(item.size()))
                .lastModified(item.lastModified().withZoneSameLocal(ZoneId.systemDefault()))
                .location(PathUtils.cutLocation(item))
                .build();
    }

    @Override
    public FolderView itemToFolderView(Item item) {
        return new FolderView(PathUtils.cutItemName(item), PathUtils.cutLocation(item));
    }
}
