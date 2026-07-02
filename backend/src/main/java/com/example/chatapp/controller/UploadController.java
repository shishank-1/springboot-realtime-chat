package com.example.chatapp.controller;

import com.example.chatapp.dto.UploadResponse;
import com.example.chatapp.service.UploadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
public class UploadController {
    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping
    public UploadResponse upload(@RequestPart("file") MultipartFile file) {
        return uploadService.upload(file);
    }
}
