package com.me.clouddrive.exception.folder;

public class DownloadFolderException extends FolderException {

    public DownloadFolderException(String folderName) {
        super("An error occurred while downloading a folder " + folderName);
    }

    public DownloadFolderException(String folderName, Throwable cause) {
        super("An error occurred while downloading a folder " + folderName, cause);
    }
}