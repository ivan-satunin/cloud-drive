package com.me.clouddrive.controller;

import com.me.clouddrive.controller.payload.RenameObjectPayload;
import com.me.clouddrive.entity.User;
import com.me.clouddrive.service.FolderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cloud-drive/folders")
public class FoldersController {

    private final FolderService folderService;

    @GetMapping(produces = "application/zip")
    public ResponseEntity<StreamingResponseBody> downloadFolder(@AuthenticationPrincipal User user, @RequestParam String path) {
        var zipFolder = folderService.downloadFolder(user.getId(), path);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + zipFolder.name() + "\"")
                .body(zipFolder::writeToZip);
    }

    @DeleteMapping
    public String deleteFolder(@AuthenticationPrincipal User user, @RequestParam String path) {
        folderService.deleteFolder(user.getId(), path);
        return "redirect:/cloud-drive";
    }

    @PatchMapping
    public String renameFolder(
            @AuthenticationPrincipal User user,
            @Valid @ModelAttribute("renameFolderPayload") RenameObjectPayload renameFolderPayload,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            //TODO
        }
        folderService.renameFolder(user.getId(), renameFolderPayload.getLocation(), renameFolderPayload.getOldName(), renameFolderPayload.getNewName());
        return "redirect:/cloud-drive";
    }
}
