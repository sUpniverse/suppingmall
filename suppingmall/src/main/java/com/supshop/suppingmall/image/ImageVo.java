package com.supshop.suppingmall.image;

import lombok.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class ImageVo {

    private boolean uploaded;
    private URI url;

}
