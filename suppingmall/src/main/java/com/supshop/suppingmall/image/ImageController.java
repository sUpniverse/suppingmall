package com.supshop.suppingmall.image;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/images")
public class ImageController {

    private static final String boardSourceUrl =  "src/main/resources/images/board/";
    public static final String productSourceUrl =  "src/main/resources/images/product/";
    private static final String boardUri =  "/images/board/";
    public static final String productUri =  "/images/product/";

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/board")
    public ResponseEntity<String> createBoardTempImages(MultipartFile file) throws IOException {
        log.debug("createBoardTempImages is called");

        if(file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        URI storedImagePath = imageService.saveImage(file, boardSourceUrl, boardUri);
        ObjectMapper objectMapper = new ObjectMapper();
        ImageVo imageVo = ImageVo.builder().uploaded(true).url(storedImagePath).build();
        return ResponseEntity.created(storedImagePath).body(objectMapper.writeValueAsString(imageVo));
    }

    @GetMapping("/board/{filePath}")
    public ResponseEntity<byte[]> getBoardImage(@PathVariable String filePath) {
        log.debug("getBoardImage is called");
        byte[] readImageBytes = imageService.getImage(boardSourceUrl + filePath);
        return ResponseEntity.ok(readImageBytes);

    }

    @PostMapping("/product")
    public ResponseEntity<String> createProductTempImages(MultipartFile file) throws IOException {
        log.debug("createProductTempImages is called");

        if(file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        URI storedImagePath = imageService.saveImage(file, productSourceUrl, productUri);
        ObjectMapper objectMapper = new ObjectMapper();
        ImageVo imageVo = ImageVo.builder().uploaded(true).url(storedImagePath).build();
        return ResponseEntity.created(storedImagePath).body(objectMapper.writeValueAsString(imageVo));
    }

    @GetMapping("/product/{filePath}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable String filePath) {
        log.debug("getProductImage is called");

        byte[] readImageBytes = imageService.getImage(productSourceUrl + filePath);
        return ResponseEntity.ok(readImageBytes);
    }
}
