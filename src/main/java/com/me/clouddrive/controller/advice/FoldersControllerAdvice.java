package com.me.clouddrive.controller.advice;

import com.me.clouddrive.exception.folder.FolderException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FoldersControllerAdvice {

    @ExceptionHandler
    public String handleFolderException(FolderException folderException, Model model) {
        model.addAttribute("message", folderException.getMessage());
        return "errors/folder-error";
    }
}
