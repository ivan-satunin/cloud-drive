package com.me.clouddrive.controller;

import com.me.clouddrive.dto.view.FileView;
import com.me.clouddrive.dto.view.FolderView;
import com.me.clouddrive.dto.view.StorageObject;
import com.me.clouddrive.entity.User;
import com.me.clouddrive.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cloud-drive/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public String search(@AuthenticationPrincipal User user, @RequestParam("q") String query, Model model) {
        List<StorageObject> searchResults = searchService.search(user.getId(), query);
        var fileResults = searchResults.stream()
                .filter(storageObject -> storageObject instanceof FileView)
                .toList();
        var folderResults = searchResults.stream()
                .filter(storageObject -> storageObject instanceof FolderView)
                .toList();
        model.addAttribute("fileResults", fileResults);
        model.addAttribute("folderResults", folderResults);
        return "search";
    }
}
