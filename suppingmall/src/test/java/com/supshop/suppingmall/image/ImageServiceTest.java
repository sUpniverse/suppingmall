package com.supshop.suppingmall.image;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageServiceTest {

    @Autowired ImageService imageService;

    @Test
    public void saveAndGetImageInStorage() throws Exception {
        //given
        Set<String> urls = new HashSet<>();
        urls.add("images/board/20200818/19/jsjs.png");
        urls.add("images/board/20200818/19/현대차.png");
        String path = "images/board/20200818/19/";
        String location = "board";

        //when
        boolean isCompleteUpload = imageService.saveInStorage(urls, path,3l, location);
        byte[] imageInStorage = imageService.getImageInStorage("board/3/jsjs.png");

        //then
        assertThat(isCompleteUpload).isTrue();
        byte[] imageInTemp = imageService.getImage("src/main/resources/" + path + "jsjs.png");
        assertThat(imageInStorage).isEqualTo(imageInTemp);

    }
}