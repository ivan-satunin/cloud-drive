package com.me.clouddrive.exception.file;

public class UploadFileException extends FileException {

    public UploadFileException(String uploadFile, Throwable cause) {
        super("An error occurred while uploading a file " + uploadFile, cause);
    }
}
