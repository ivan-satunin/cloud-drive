package com.me.clouddrive.exception.file;

public class FileNotFoundException extends FileException {

    public FileNotFoundException(String name) {
        super("Not found file: " + name);
    }
}
