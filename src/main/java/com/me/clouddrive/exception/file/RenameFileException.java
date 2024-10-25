package com.me.clouddrive.exception.file;

public class RenameFileException extends FileException {

    public RenameFileException(String oldFileName, Throwable cause) {
        super("An error occurred while renaming a file " + oldFileName, cause);
    }
}
