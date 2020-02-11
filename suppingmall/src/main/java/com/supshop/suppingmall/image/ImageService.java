package com.supshop.suppingmall.image;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;

@Service
public class ImageService {

    public URI saveImage(MultipartFile file, String sourceUrl, String uri) throws IOException {

        String originalFilename = file.getOriginalFilename();
        String storedName = LocalDateTime.now()+ "_" + originalFilename;
        String uploadImageUrl =  sourceUrl + storedName;
        byte[] image = file.getBytes();
        FileOutputStream fos = new FileOutputStream(uploadImageUrl);
        fos.write(image);
        fos.close();
        URI imageUri = URI.create(uri + storedName);

        return URI.create(imageUri.toString());
    }

    public byte[] getImage(String path) {
        byte[] readImageBytes = new byte[0];
        try {
            FileInputStream fis = new FileInputStream(path);
            readImageBytes = fis.readAllBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readImageBytes;
    }
}
