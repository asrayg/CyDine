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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Images", description = "Image management APIs")
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
    @Operation(summary = "Get image by name", description = "Retrieves an image file by its name.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "image/jpeg, image/png"))
    @ApiResponse(responseCode = "404", description = "Image not found")
    Resource getImageById(@Parameter(description = "Name of the image file") @RequestParam("image") String image) throws IOException {
        File file = new File(directory + image);
        Resource resource = new FileSystemResource(file);
        return resource;
    }

    @PostMapping("/images")
    @Operation(summary = "Upload an image", description = "Uploads a new image file.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(type = "integer")))
    @ApiResponse(responseCode = "400", description = "Bad request")
    public int handleFileUpload(@Parameter(description = "Image file to upload") @RequestParam("image") MultipartFile imageFile) {
        try {
            File uploadir = new File(directory);
            if(!uploadir.exists()){
                uploadir.mkdirs();
            }
            String filename = imageFile.getOriginalFilename();
            File destinationFile = new File(directory + filename);
            Files.copy(imageFile.getInputStream(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            imageFile.transferTo(destinationFile);  // save file to disk
            return 1;
        } catch (IOException e) {
            return -1;
        }
    }

    public ImageController getImagesHere() {
        return imagesHere;
    }
}
