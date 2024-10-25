package com.me.clouddrive.mapper;

import com.me.clouddrive.dto.view.FileView;
import com.me.clouddrive.dto.view.FolderView;
import io.minio.messages.Item;

public interface MinioItemMapper {

    FileView itemToFileView(Item item);

    FolderView itemToFolderView(Item item);
}
