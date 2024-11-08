package CyDine.ChatImage;

import CyDine.Fitness.Fitness;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.springframework.core.io.*;

@RestController
public class ImageController {

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private List<Image> images;

    @ManyToOne
    private CyDine.ChatImage.ImageController imagesHere;

    // replace this! careful with the operating system in use
    private static String directory = "lo/";

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping(value = "/images/gets", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    Resource getImageById(@RequestParam ("image") String image) throws IOException {

        File file = new File(directory + image);

        Resource resource = new FileSystemResource(file);

        return resource;

        //Image image = imageRepository.findById(id);
        //File imageFile = new File(image.getFilePath());
        //return Files.readAllBytes(imageFile.toPath());
    }

    @PostMapping("/images")
    public int handleFileUpload(@RequestParam("image") MultipartFile imageFile)  {

        try {

            File uploadir = new File(directory);

            if(!uploadir.exists()){
                uploadir.mkdirs();
            }
            String filename = imageFile.getOriginalFilename();
            File destinationFile = new File(directory + filename);
            Files.copy(imageFile.getInputStream(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            imageFile.transferTo(destinationFile);  // save file to disk

            // Save the image in the repository

            //imageRepository.save(image);
            return 1;
        } catch (IOException e) {
            return -1;
        }
    }

    public ImageController getImagesHere() {
        return imagesHere;
    }

}
