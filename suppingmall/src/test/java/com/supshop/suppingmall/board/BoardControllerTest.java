package com.supshop.suppingmall.board;

import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BoardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    BoardService boardService;

    private MockHttpSession session;

    @Test
    public void getAllBoard() throws Exception {
        //then
        mockMvc.perform(get("/boards"))
                .andExpect(status().isOk());
    }

    @Test
    public void getBoard() throws Exception {
        //given
        int id = 2;

        //then
        mockMvc.perform(get("/boards/{id}",id))
                .andExpect(status().isOk());
    }

    @Test
    public void getBoardNotFound() throws Exception {
        //given
        int id = 1;

        //then
        mockMvc.perform(get("/boards/{id}",id))
                .andExpect(redirectedUrl("/boards"));
    }

    @Test
    public void getBoardWriteFormWithOutLogin() throws Exception {
        //then
        mockMvc.perform(get("/boards/form"))
                .andExpect(redirectedUrl("/users/loginform"));
    }

    @Test
    public void getBoardWriteFormWithLogin() throws Exception {
        //given
        User user = addUser();

        //when
        addUserInSession(user);

        //then
        mockMvc.perform(get("/boards/form").session(session))
                .andExpect(status().isOk());
    }

    private User addUser() {

        return User.builder()
                .userId(1l)
                .build();
    }

    private void addUserInSession(User user) {
        session = new MockHttpSession();
        session.setAttribute("user",user);

    }

    @Test
    @Transactional
    public void createBoardWithLogin() throws Exception {
        //given
        User user = addUser();
        Category category = Category.builder().id(1l).build();
        Board board = Board.builder()
                .title("test")
                .contents("test")
                .category(category)
                .creator(user)
                .build();

        //when
        addUserInSession(user);

        //then
        mockMvc.perform(post("/boards")
                                                .param("title",board.getTitle())
                                                .param("contents",board.getContents())
                                                .param("category.id",board.getCategory().getId().toString())
                                                .param("creator.userId",board.getCreator().getUserId().toString()))
                .andDo(print())
                .andExpect(redirectedUrl("/boards"));
    }

    @Test
    @Transactional
    public void addBoard() throws Exception {
        //given
        //given
        User user = addUser();
        Category category = Category.builder().id(1l).build();
        Board board = Board.builder()
                .title("test")
                .contents("test")
                .category(category)
                .creator(user)
                .build();

        //when

        boardService.createBoard(board);
        //then

    }

    @Test
    public void createBoardWithOutLogin() throws Exception {
        //given
        User user = addUser();
        Category category = Category.builder().id(1l).build();
        Board board = Board.builder()
                .title("test")
                .contents("test")
                .category(category)
                .creator(user)
                .build();

        //when

        //then
        mockMvc.perform(post("/boards")
                .param("title",board.getTitle())
                .param("contents",board.getContents())
                .param("category.id",board.getCategory().getId().toString())
                .param("creator.userId",board.getCreator().getUserId().toString()))
                .andDo(print())
                .andExpect(redirectedUrl("/boards"));
    }

    @Test
    public void createBoardWithLoginDifferUser() throws Exception {
        //given
        User user = addUser();
        Category category = Category.builder().id(1l).build();
        Board board = Board.builder()
                .title("test")
                .contents("test")
                .category(category)
                .creator(user)
                .build();

        user.setUserId(5L);
        //when
        addUserInSession(user);
        //then
        mockMvc.perform(post("/boards")
                .param("title",board.getTitle())
                .param("contents",board.getContents())
                .param("category.id",board.getCategory().getId().toString())
                .param("creator.userId",board.getCreator().getUserId().toString()))
                .andDo(print())
                .andExpect(redirectedUrl("/boards"));
    }



}
