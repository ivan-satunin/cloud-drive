package com.me.clouddrive.exception.folder;

public class FolderNotFoundException extends FolderException {

    public FolderNotFoundException(String name) {
        super("Not found folder: " + name);
    }

}
