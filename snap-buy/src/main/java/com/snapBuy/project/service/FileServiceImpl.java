package com.snapBuy.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

// Service responsible for handling file upload operations.

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {

        // Retrieve original filename from uploaded file
        String originalFileName = file.getOriginalFilename();

        // Generate unique identifier to avoid filename collisions
        String randomId = UUID.randomUUID().toString();

        // Create unique filename while preserving original file extension
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));

        // Build complete file storage path
        String filePath = path + File.separator + fileName;

        // Create upload directory if it does not already exist
        File folder = new File(path);
        if (!folder.exists())
            folder.mkdir();

        // Save uploaded file to the target location
        Files.copy(file.getInputStream(), Paths.get(filePath));


        return fileName;
    }
}