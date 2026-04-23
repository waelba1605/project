package com.elearning.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileUploadUtil {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Value("${file.max-size:10485760}")
    private long maxFileSize;

    @Value("${file.allowed-extensions:pdf,doc,docx,txt,jpg,jpeg,png,gif,mp4,avi}")
    private String allowedExtensions;

    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("File size exceeds maximum limit of " + maxFileSize + " bytes");
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);

        if (!isAllowedExtension(fileExtension)) {
            throw new IllegalArgumentException("File type not allowed");
        }

        String uploadDirectory = uploadDir;
        File uploadPath = new File(uploadDirectory);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        String fileName = UUID.randomUUID() + "." + fileExtension;
        Path filePath = Paths.get(uploadDirectory, fileName);
        Files.write(filePath, file.getBytes());

        return fileName;
    }

    public void deleteFile(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir, fileName);
        Files.deleteIfExists(filePath);
    }

    public byte[] downloadFile(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir, fileName);
        return Files.readAllBytes(filePath);
    }

    private String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf(".");
        return (lastDot > 0) ? filename.substring(lastDot + 1).toLowerCase() : "";
    }

    private boolean isAllowedExtension(String extension) {
        String[] extensions = allowedExtensions.split(",");
        for (String ext : extensions) {
            if (ext.trim().equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }
}
