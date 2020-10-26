package com.supshop.suppingmall.board;

import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.category.CategoryFactory;
import com.supshop.suppingmall.category.CategoryService;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class BoardServiceTest {

    @Autowired BoardService boardService;
    @Autowired UserFactory userFactory;
    @Autowired CategoryFactory categoryFactory;


    public Board buildBoard() {
        User user = userFactory.createUser("user");
        Category category = categoryFactory.createCategory("board");
        return Board.builder()
                .title("테스트")
                .contents("테스트 글")
                .category(category)
                .creator(user)
                .build();
    }

    @Test
    public void createBoard() throws Exception {
        //given
        int size = boardService.getBoards(null, null).size();
        Board board = buildBoard();
        board.setContents(board.getContents());
        //when
        boardService.createBoard(board,null);
        int addedSize = boardService.getBoards(null, null).size();

        //then
        assertThat(addedSize).isEqualTo(size+1);

    }

    @Test
    public void createBoard_실패_없는_이미지() throws Exception {
        //given
        int size = boardService.getBoards(null, null).size();
        Board board = buildBoard();
        Set<String> urls = new HashSet<>();
        String imageUrl = "<img src=\\\"image/board/20200818/32/jsjs.jpg\\\">";
        urls.add(imageUrl);
        board.setContents(board.getContents()+imageUrl);
        //when
        boardService.createBoard(board,urls);
        int addedSize = boardService.getBoards(null, null).size();

        //then
        assertThat(addedSize).isEqualTo(size+1);

    }



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