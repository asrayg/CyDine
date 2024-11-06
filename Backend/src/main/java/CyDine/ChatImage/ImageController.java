package CyDine.ChatImage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
public class ImageController {

    // replace this! careful with the operating system in use
    private static String directory = "src/main/java/CyDine/images";

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping(value = "/images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    byte[] getImageById(@PathVariable int id) throws IOException {
        Image image = imageRepository.findById(id);
        File imageFile = new File(image.getFilePath());
        return Files.readAllBytes(imageFile.toPath());
    }

    @PostMapping("/images")
    public int handleFileUpload(@RequestParam("image") MultipartFile imageFile)  {

        try {
            File destinationFile = new File(directory + File.separator + imageFile.getOriginalFilename());
            imageFile.transferTo(destinationFile);  // save file to disk

            Image image = new Image();
            image.setFilePath(destinationFile.getAbsolutePath());
            imageRepository.save(image);

            return image.getId();
        } catch (IOException e) {
            return -1;
        }
    }

}
