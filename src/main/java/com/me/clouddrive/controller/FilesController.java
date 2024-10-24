package com.me.clouddrive.controller;

import com.me.clouddrive.controller.payload.RenameObjectPayload;
import com.me.clouddrive.entity.User;
import com.me.clouddrive.service.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cloud-drive/files")
public class FilesController {
    private final FileService fileService;

    @GetMapping
    public ResponseEntity<Resource> downloadFile(@AuthenticationPrincipal User user, @RequestParam String path) {
        var downloadFile = fileService.getDownloadFileData(user.getId(), path);
        var filename = UriUtils.encode(downloadFile.name(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(downloadFile.fileData()));
    }

    @DeleteMapping
    public String deleteFile(
            @AuthenticationPrincipal User user,
            @RequestParam String path
    ) {
        fileService.deleteFile(user.getId(), path);
        return "redirect:/cloud-drive";
    }

    @PatchMapping
    public String renameFile(
            @AuthenticationPrincipal User user,
            @Valid @ModelAttribute("renameFilePayload") RenameObjectPayload renameFilePayload,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            //TODO
        }
        fileService.renameFile(user.getId(), renameFilePayload.getLocation(), renameFilePayload.getOldName(), renameFilePayload.getNewName());
        return "redirect:/cloud-drive";
    }
}
