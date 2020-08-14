package com.supshop.suppingmall.image;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ImageService {

    public URI saveImage(MultipartFile file, String sourceUrl, String uri, Long userId) throws IOException {

        String imageName = file.getOriginalFilename().trim().replaceAll("\\p{Z}","");
        // ex) sourceUrl + 2020/8/12/유저번호/이미지 이름
        StringBuilder uploadImageUrlMaker = new StringBuilder();
        uploadImageUrlMaker.append(sourceUrl)
                     .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))).append(File.separator)
                     .append(userId);

        File folder = new File(uploadImageUrlMaker.toString());
        if(!folder.exists()) {
            folder.mkdirs();
        }
        uploadImageUrlMaker.append(File.separator).append(imageName);
        byte[] image = file.getBytes();
        FileOutputStream fos = new FileOutputStream(uploadImageUrlMaker.toString());
        fos.write(image);
        fos.close();

        uploadImageUrlMaker.delete(0, sourceUrl.length());

        URI imageUri = URI.create(uri + uploadImageUrlMaker.toString());

        return imageUri;
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

    /*Todo : 받은 url을 통해 그림을 Storage에 저장
    public void saveInStorage(Set<String> pathUrls){

    }*/

    /*
    * 업로드된 이미지를 임시 저장소에서 List 형태로 받아온다.
    * */
    public List<byte[]> getImages(Set<String> pathUrls) {
        List<byte[]> byteList = new ArrayList<>();
        for(String path : pathUrls) {
            byte[] bytes = getImage(path);
            byteList.add(bytes);
        }
        return byteList;
    }

}
