package com.me.clouddrive.exception.folder;

public class RenameFolderException extends FolderException {

    public RenameFolderException(String oldFolderName, Throwable cause) {
        super("An error occurred while renaming a folder " + oldFolderName, cause);
    }
}
