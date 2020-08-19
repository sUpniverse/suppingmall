package com.supshop.suppingmall.board;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class BoardServiceTest {

    @Autowired BoardService boardService;

    @Test
    public void board_이미지_Url_변경() throws Exception {
        //given
        Set<String> url = new HashSet<>();
        url.add("image/board/20200818/32/jsjs.jpg");
        Board board = Board.builder()
                .boardId(3l)
                .contents("<img src=\"image/board/20200818/32/jsjs.jpg\">")
                .build();

        //when
        String urlString = boardService.setBoardImageUrl(board,url);

        //then
        assertThat(urlString).isEqualTo("image/board/20200818/32/");
        assertThat("<img src=\"image/board/"+board.getBoardId()+ File.separator+"jsjs.jpg\">")
                    .isEqualTo(board.getContents());

    }
}