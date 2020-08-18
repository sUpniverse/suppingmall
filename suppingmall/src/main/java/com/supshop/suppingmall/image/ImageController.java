package com.supshop.suppingmall.image;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supshop.suppingmall.user.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    private static final String boardSourceUrl =  "src/main/resources/images/board/";
    public static final String productSourceUrl =  "src/main/resources/images/product/";
    private static final String boardUri =  "/images/board/";
    public static final String productUri =  "/images/product/";

    @PostMapping("/board/{userId}")
    public ResponseEntity<String> createBoardTempImages(MultipartFile file,
                                                        @PathVariable Long userId) {

        log.debug("createBoardTempImages is called");
        ResponseEntity<String> response = null;
        try {
            response = getResponse(file, boardSourceUrl, boardUri, userId);
        } catch (Exception e) {
            ResponseEntity.badRequest().build();
        }
        return response;
    }

    @GetMapping("/board/{date}/{userId}/{filePath}")
    public ResponseEntity<byte[]> getTempBoardImage(@PathVariable int date,
                                                    @PathVariable Long userId,
                                                    @PathVariable String filePath) {
        log.debug("getBoardImage is called");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(date).append(File.separator).append(userId).append(File.separator).append(filePath);
        byte[] readImageBytes = null;
        try {
            readImageBytes = imageService.getImage(boardSourceUrl + stringBuilder.toString());
        } catch (IOException e){
            ResponseEntity.notFound();
        }
        return ResponseEntity.ok(readImageBytes);
    }

    @GetMapping("/board/{filePath}")
    public ResponseEntity<byte[]> getBoardImage(@PathVariable String filePath) {
        log.debug("getBoardImage is called");
        byte[] readImageBytes = null;
        try {
            readImageBytes = imageService.getImage(boardSourceUrl + filePath);
        } catch (IOException e) {
            ResponseEntity.notFound();
        }
        return ResponseEntity.ok(readImageBytes);
    }

    @PostMapping("/product/{userId}")
    public ResponseEntity<String> createProductTempImages(MultipartFile file,
                                                          @PathVariable Long userId){
        log.debug("createProductTempImages is called");
        ResponseEntity response = null;
        try {
            response = getResponse(file, productSourceUrl, productUri, userId);
        } catch (Exception e) {
            ResponseEntity.badRequest().build();
        }
        return response;
    }

    @GetMapping("/product/{date}/{userId}/{filePath}")
    public ResponseEntity<byte[]> getTempProductImage(@PathVariable int date,
                                                  @PathVariable Long userId,
                                                  @PathVariable String filePath) {
        log.debug("getProductImage is called");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(date).append(File.separator).append(userId).append(File.separator).append(filePath);
        byte[] readImageBytes = null;
        try {
            readImageBytes = imageService.getImage(productSourceUrl + stringBuilder.toString());
        } catch (IOException e) {
            ResponseEntity.notFound();
        }
        return ResponseEntity.ok(readImageBytes);
    }


    @GetMapping("/product/{filePath}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable String filePath) {
        log.debug("getProductImage is called");
        byte[] readImageBytes = null;
        try {
            readImageBytes = imageService.getImage(productSourceUrl + filePath);
        } catch (IOException e) {
            ResponseEntity.notFound();
        }
        return ResponseEntity.ok(readImageBytes);
    }

    private ResponseEntity<String> getResponse(MultipartFile file, String sourceUrl, String uri, Long userId) throws IOException {

        if(file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        URI storedImagePath = imageService.saveImage(file, sourceUrl, uri, userId);
        ObjectMapper objectMapper = new ObjectMapper();
        ImageVo imageVo = ImageVo.builder().uploaded(true).url(storedImagePath).build();
        return ResponseEntity.created(storedImagePath).body(objectMapper.writeValueAsString(imageVo));
    }
}
