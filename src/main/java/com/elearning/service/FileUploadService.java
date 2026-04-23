package com.elearning.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${file.upload.dir:uploads}")
    private String uploadDir;

    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final long MAX_DOCUMENT_SIZE = 10 * 1024 * 1024; // 10MB
    private static final long MAX_VIDEO_SIZE = 500 * 1024 * 1024; // 500MB

    private static final String[] ALLOWED_IMAGE_TYPES = {"image/jpeg", "image/png", "image/gif", "image/webp"};
    private static final String[] ALLOWED_DOCUMENT_TYPES = {"application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};
    private static final String[] ALLOWED_VIDEO_TYPES = {"video/mp4", "video/mpeg", "video/quicktime", "video/x-msvideo"};

    public String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }
        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new IOException("Image size exceeds maximum limit of 5MB");
        }
        if (!isAllowedType(file.getContentType(), ALLOWED_IMAGE_TYPES)) {
            throw new IOException("Invalid image type. Allowed types: JPG, PNG, GIF, WebP");
        }

        return saveFile(file, "images");
    }

    public String uploadDocument(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }
        if (file.getSize() > MAX_DOCUMENT_SIZE) {
            throw new IOException("Document size exceeds maximum limit of 10MB");
        }
        if (!isAllowedType(file.getContentType(), ALLOWED_DOCUMENT_TYPES)) {
            throw new IOException("Invalid document type. Allowed types: PDF, DOC, DOCX");
        }

        return saveFile(file, "documents");
    }

    public String uploadVideo(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }
        if (file.getSize() > MAX_VIDEO_SIZE) {
            throw new IOException("Video size exceeds maximum limit of 500MB");
        }
        if (!isAllowedType(file.getContentType(), ALLOWED_VIDEO_TYPES)) {
            throw new IOException("Invalid video type. Allowed types: MP4, MPEG, MOV, AVI");
        }

        return saveFile(file, "videos");
    }

    public void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.deleteIfExists(path);
    }

    private String saveFile(MultipartFile file, String subfolder) throws IOException {
        // Create directory if not exists
        File uploadDirectory = new File(uploadDir + File.separator + subfolder);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        // Save file
        Path filePath = Paths.get(uploadDir, subfolder, uniqueFilename);
        Files.write(filePath, file.getBytes());

        // Return file path
        return filePath.toString();
    }

    private boolean isAllowedType(String contentType, String[] allowedTypes) {
        for (String type : allowedTypes) {
            if (type.equals(contentType)) {
                return true;
            }
        }
        return false;
    }
}
