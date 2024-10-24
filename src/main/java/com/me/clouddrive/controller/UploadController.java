package com.me.clouddrive.controller;

import com.me.clouddrive.entity.User;
import com.me.clouddrive.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cloud-drive/upload")
public class UploadController {

    private final FileService fileService;

    @PostMapping
    public String upload(
            @AuthenticationPrincipal User user,
            @RequestParam List<MultipartFile> files,
            @RequestParam(required = false, defaultValue = "") String path
    ) {
        files.forEach(file -> fileService.uploadFile(user.getId(), file, path));
        return "redirect:/cloud-drive";
    }
}
