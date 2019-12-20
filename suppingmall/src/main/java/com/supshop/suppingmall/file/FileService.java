package com.supshop.suppingmall.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class FileService {

    @Value("${upload.dir:${user.home]}")
    private String uploadDir;

    public String uploadFile(MultipartFile file, String type, Long id) {
        Path copyLocation = Paths.get(uploadDir + File.pathSeparator + type
                + File.pathSeparator + id + File.pathSeparator + StringUtils.cleanPath(file.getOriginalFilename()));
        try {
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return copyLocation.toString();
    }
}
