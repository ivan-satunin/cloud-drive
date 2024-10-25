package com.me.clouddrive.controller.advice;

import com.me.clouddrive.exception.file.FileException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FilesControllerAdvice {

    @ExceptionHandler
    public String handleFileException(FileException fileException, Model model) {
        model.addAttribute("message", fileException.getMessage());
        return "errors/file-error";
    }
}
