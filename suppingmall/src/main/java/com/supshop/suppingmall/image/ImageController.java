package com.supshop.suppingmall.image;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;

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
    private static final String boardName = "board";
    private static final String productName = "product";

    @PostMapping("/board/{userId}")
    public ResponseEntity<String> createBoardTempImages(MultipartFile file,
                                                        @PathVariable Long userId) {

        log.debug("createBoardTempImages is called");
        ResponseEntity<String> response = null;
        response = saveImageInTempProcess(file, boardSourceUrl, boardUri, userId);
        return response;
    }

    @GetMapping("/board/{date}/{userId}/{filePath}")
    public ResponseEntity<byte[]> getTempBoardImage(@PathVariable int date,
                                                    @PathVariable Long userId,
                                                    @PathVariable String filePath) {
        log.debug("getBoardImage is called");
        ResponseEntity<byte[]> response = getImageInTempProcess(date, userId, filePath, boardSourceUrl);
        return response;
    }

    @GetMapping("/board/{boardId}/{filePath}")
    public ResponseEntity<byte[]> getBoardImage(@PathVariable Long boardId,
                                                @PathVariable String filePath) {
        log.debug("getBoardImage is called");
        byte[] readImageBytes = null;
        readImageBytes = imageService.getImageInStorage(boardName+File.separator+boardId+File.separator+filePath);
        return ResponseEntity.ok(readImageBytes);
    }

    @PostMapping("/product/{userId}")
    public ResponseEntity<String> createProductTempImages(MultipartFile file,
                                                          @PathVariable Long userId){
        log.debug("createProductTempImages is called");
        ResponseEntity response = null;
        response = saveImageInTempProcess(file, productSourceUrl, productUri, userId);
        return response;
    }

    @GetMapping("/product/{date}/{userId}/{filePath}")
    public ResponseEntity<byte[]> getTempProductImage(@PathVariable int date,
                                                  @PathVariable Long userId,
                                                  @PathVariable String filePath) {
        log.debug("getProductImage is called");
        ResponseEntity<byte[]> response = getImageInTempProcess(date, userId, filePath, productSourceUrl);
        return response;
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

    @GetMapping("/product/{productId}/{filePath}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long productId,
                                                @PathVariable String filePath) {
        log.debug("getProductImage is called");
        byte[] readImageBytes = null;
        readImageBytes = imageService.getImageInStorage(productName +File.separator+productId+File.separator+filePath);
        return ResponseEntity.ok(readImageBytes);
    }

    // board와 product 이미지를 임시 저장하기 위한 통합 프로세스
    private ResponseEntity<String> saveImageInTempProcess(MultipartFile file, String sourceUrl, String uri, Long userId){

        URI storedImagePath = null;

        if(file.isEmpty()) return ResponseEntity.badRequest().build();

        try {
            storedImagePath = imageService.saveImage(file, sourceUrl, uri, userId);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ImageVo imageVo = ImageVo.builder().uploaded(true).url(storedImagePath).build();
        ResponseEntity<String> response;

        try {
            response = ResponseEntity.created(storedImagePath).body(objectMapper.writeValueAsString(imageVo));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return response;
    }

    // board와 product의 이미지를 임시저장소에서 가져오기 위한 통합 프로세스
    private ResponseEntity<byte[]> getImageInTempProcess(@PathVariable int date, @PathVariable Long userId,
                                                         @PathVariable String filePath, String boardSourceUrl) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(date).append(File.separator).append(userId).append(File.separator).append(filePath);
        byte[] readImageBytes = null;
        try {
            readImageBytes = imageService.getImage(boardSourceUrl + stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(readImageBytes);
    }

}
