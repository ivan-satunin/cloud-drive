package com.me.clouddrive.dto.view;

public record FolderView(
        String name,
        String location
) implements StorageObject {
}
