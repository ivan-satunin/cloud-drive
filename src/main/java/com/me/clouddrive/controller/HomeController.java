package com.me.clouddrive.controller;

import com.me.clouddrive.controller.payload.RenameObjectPayload;
import com.me.clouddrive.entity.User;
import com.me.clouddrive.service.FileService;
import com.me.clouddrive.service.FolderService;
import com.me.clouddrive.service.MinioUserObjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cloud-drive")
public class HomeController {

    private final FileService fileService;
    private final FolderService folderService;

    @GetMapping
    public String homePage(
            @AuthenticationPrincipal User user,
            Model model,
            @RequestParam(required = false, defaultValue = MinioUserObjectService.ROOT_FOLDER) String path
    ) {
        if (user != null) {
            var files = fileService.findAllUserFiles(user.getId(), path);
            var folders = folderService.findUserFolders(user.getId(), path);
            var usedSpace = fileService.calcUsedSpace(user.getId());
            model.addAttribute("files", files);
            model.addAttribute("usedSpace", usedSpace);
            model.addAttribute("folders", folders);
            model.addAttribute("renameFilePayload", new RenameObjectPayload());
            model.addAttribute("renameFolderPayload", new RenameObjectPayload());
        }
        return "home";
    }

}
