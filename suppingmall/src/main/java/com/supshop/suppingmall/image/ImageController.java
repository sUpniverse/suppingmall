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

    @PostMapping("/board")
    public ResponseEntity<String> createBoardTempImages(MultipartFile file) throws IOException {
        log.debug("createBoardTempImages is called");

        if(file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String originalFilename = file.getOriginalFilename();
        String storedName = LocalDateTime.now()+ "_" + originalFilename;
        String uploadImageUrl =  boardSourceUrl + storedName;
        byte[] image = file.getBytes();
        FileOutputStream fos = new FileOutputStream(uploadImageUrl);
        fos.write(image);
        fos.close();
        URI imageUri = URI.create("/images/board/" + storedName);
        ObjectMapper objectMapper = new ObjectMapper();
        URI storedImagePath = URI.create(imageUri.toString());
        ImageVo imageVo = ImageVo.builder().uploaded(true).url(storedImagePath).build();
        return ResponseEntity.created(imageUri).body(objectMapper.writeValueAsString(imageVo));
    }

    @GetMapping("/board/{filePath}")
    public ResponseEntity<byte[]> getBoardImage(@PathVariable String filePath) {
        log.debug("getBoardImage is called");
        byte[] readImageBytes = new byte[0];
        try {
            FileInputStream fis = new FileInputStream(boardSourceUrl + filePath);
            readImageBytes = fis.readAllBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(readImageBytes);

    }

}
