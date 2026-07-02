package com.example.chatapp.service;

import com.cloudinary.Cloudinary;
import com.example.chatapp.dto.UploadResponse;
import com.example.chatapp.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Service
public class UploadService {
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "mp4");
    private final Cloudinary cloudinary;

    public UploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public UploadResponse upload(MultipartFile file) {
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "File is required");
        }
        String extension = extension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Only jpg, jpeg, png, gif and mp4 are supported");
        }
        String resourceType = extension.equals("mp4") ? "video" : "image";
        try {
            var result = cloudinary.uploader().upload(file.getBytes(), Map.of("resource_type", resourceType, "folder", "private-chat"));
            return new UploadResponse((String) result.get("secure_url"), resourceType);
        } catch (IOException e) {
            throw new ApiException(HttpStatus.BAD_GATEWAY, "Cloudinary upload failed");
        }
    }

    private String extension(String filename) {
        int index = filename.lastIndexOf('.');
        return index == -1 ? "" : filename.substring(index + 1).toLowerCase();
    }
}
