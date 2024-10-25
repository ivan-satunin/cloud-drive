package com.me.clouddrive.service;

import com.me.clouddrive.dto.view.StorageObject;

import java.util.List;

public interface SearchService {

    List<StorageObject> search(long userId, String query);

}
