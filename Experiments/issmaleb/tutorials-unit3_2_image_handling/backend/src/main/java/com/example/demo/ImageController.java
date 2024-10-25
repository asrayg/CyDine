package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
public class ImageController {

    // Set directory path to user home + /uploads
    private static String directory = System.getProperty("user.home") + "/uploads";

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping(value = "/images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    byte[] getImageById(@PathVariable int id) throws IOException {
        Image image = imageRepository.findById(id);
        File imageFile = new File(image.getFilePath());
        return Files.readAllBytes(imageFile.toPath());
    }

    @PostMapping("images")
    public String handleFileUpload(@RequestParam("image") MultipartFile imageFile)  {
        File directoryPath = new File(directory);

        // Create directory if it doesn't exist
        if (!directoryPath.exists()) {
            directoryPath.mkdirs();
        }

        try {
            File destinationFile = new File(directoryPath, imageFile.getOriginalFilename());
            imageFile.transferTo(destinationFile);  // Save file to disk

            Image image = new Image();
            image.setFilePath(destinationFile.getAbsolutePath());
            imageRepository.save(image);

            return "File uploaded successfully: " + destinationFile.getAbsolutePath();
        } catch (IOException e) {
            return "Failed to upload file: " + e.getMessage();
        }
    }
}
