package com.me.clouddrive.exception.file;

public class DeleteFileException extends FileException {

    public DeleteFileException(String fileName) {
        super("An error occurred while deleting a file " + fileName);
    }
}
