package com.me.clouddrive.dto.view;

public interface StorageObject {

    String name();

    String location();

    default String path() {
        return location().isEmpty() ? name() : location() + "/" + name();
    }
}
