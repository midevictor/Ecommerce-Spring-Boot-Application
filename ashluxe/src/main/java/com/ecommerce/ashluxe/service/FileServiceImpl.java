package com.ecommerce.ashluxe.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{

  @Override
  public String uploadImage(String path, MultipartFile image) throws IOException {
        /// file name of current / orifgonal file
        /// generate a unique file name
        /// check if path exist or cerate it
        /// upload it to sever

        // Validate file existence and original name
        if (image == null || image.isEmpty() || image.getOriginalFilename() == null) {
            throw new IllegalArgumentException("Invalid image file");
        }

        /// Get the original filename and generate a unique name
        String originalFileName = image.getOriginalFilename();
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf(".")));

        /// Correctly construct the full path
        String fullPath = Paths.get(path, fileName).toString();

        /// Check if the directory exists and create it if necessary
        File folder = new File(path);
        if (!folder.exists()) {
            if (!folder.mkdirs()) { // Use mkdirs() to create all missing parent directories
                throw new IOException("Unable to create directory: " + path);
            }
        }

// Copy the file to the destination
        Files.copy(image.getInputStream(), Paths.get(fullPath));

        return fileName;

    }
}
