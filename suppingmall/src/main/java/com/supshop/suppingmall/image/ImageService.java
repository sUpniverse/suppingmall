package com.supshop.suppingmall.image;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {

    private final Storage storage;

    private static final String storageBucketName = "suppingmall-image-storage-1";
    private static final String classPath = "src/main/resources";

    @Transactional
    public URI saveImage(MultipartFile file, String sourceUrl, String uri, Long userId) throws IOException {

        String imageName = file.getOriginalFilename().trim().replaceAll("\\p{Z}","");
        // ex) sourceUrl + yyyyMMdd/유저번호/파일명
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

    public byte[] getImage(String path) throws IOException {
        byte[] readImageBytes;
        FileInputStream fis = new FileInputStream(path);
        readImageBytes = fis.readAllBytes();
        return readImageBytes;
    }

    // Todo : 받은 url을 통해 그림을 Storage에 저장 /board/boardId/fileName
    @Transactional
    public boolean saveInStorage(Set<String> pathUrls, String path, Long Id, String directory){
        try {
            for(String url : pathUrls) {
                String fileName = url.replace(path, "");
                storage.create(BlobInfo.newBuilder(storageBucketName,directory+File.separator+Id+File.separator+fileName)
                        .build(),
                        getImage(classPath+File.separator+url));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
