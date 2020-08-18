package com.supshop.suppingmall.board;

import com.supshop.suppingmall.board.form.BoardCreateForm;
import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.category.CategoryService;
import com.supshop.suppingmall.comment.CommentService;
import com.supshop.suppingmall.image.ImageService;
import com.supshop.suppingmall.mapper.BoardMapper;
import com.supshop.suppingmall.page.BoardCriteria;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardMapper boardMapper;
    private final CategoryService categoryService;
    private final ImageService imageService;

    private static final String boardName = "board";
    private static final String boardImageUrl = "image/board/";

    public List<Board> getAllBoard() { return boardMapper.selectAllBoard(); }

    public List<Board> getBoardByCondition(BoardCriteria boardCriteria, String category, String type, String searchValue) {
        if(category == null) {
            Category categoryByEnName = categoryService.getCategoryByEnName(boardName);
            return boardMapper.selectBoardByCondition(boardCriteria,categoryByEnName.getId(), type, searchValue);
        }
        Category categoryByEnName = categoryService.getCategoryByEnName(category);
        return boardMapper.selectBoardByCondition(boardCriteria,categoryByEnName.getId(), type, searchValue);
    }

    public int getBoardCount() {
        return boardMapper.selectBoardCount();
    }

    @Transactional
    public Board getBoard(Long id) {
        Optional<Board> board = boardMapper.selectBoard(id);
        if(!board.isEmpty()) {
            boardMapper.updateBoardHit(id);
            return board.get();
        }
        return null;
    }

    public Board getBoardByProduct(Long id) {
        Optional<Board> board = boardMapper.selectBoard(id);
        if(!board.isEmpty()) {
            return board.get();
        }
        return null;
    }

    /*
     * Todo : ImageService 이용하여 GCP Cloud에 이미지 업로드 후 해당 board 내용에서 경로 수정
     *  1. DB에 해당 board 저장
     *  2. board의 내용에서 <img src> 태그 찾아서 해당 업로드 된 이미지 찾기
     *  3. 찾은 경로를 이용해 임시 저장된 이미지를 List로 모아서 ImageService에서 GCP Storage로 저장 With 해당 게시글 번호 (폴더 이름으로 쓸 예정)
     */
    @Transactional
    public void createBoard(Board board) {
        int imageCount = board.getImagesUrl().size();
        String originUrl = null;
        int result = boardMapper.insertBoard(board);
        if(imageCount > 0 && result == 1){
            originUrl = setBoardUrl(board);
            boardMapper.updateBoard(board.getBoardId(), board);
            imageService.saveInStorage(board.getImagesUrl(),originUrl,board.getBoardId(), boardName);
        }
    }

    // image/{category}/{yyyyMMdd}/{userId}/fileName => image/{category}/{categoryId}/fileName
    // cloud storage의 경로 저장을 위해 이미지 url 변경
    public String setBoardUrl(Board board) {
        String originUrl;
        String contents = board.getContents();
        String imageUrl = board.getImagesUrl().iterator().next();
        String[] splitUrl = imageUrl.split(File.separator);
        int fileIndex = imageUrl.indexOf(splitUrl[splitUrl.length-1]);
        originUrl = imageUrl.substring(0,fileIndex);
        contents = contents.replace(originUrl, boardImageUrl+board.getBoardId()+File.separator);
        board.setContents(contents);
        return originUrl;
    }

    @Transactional
    public void updateBoard(Long id, Board board) {
        boardMapper.updateBoard(id, board);
    }

    @Transactional
    public void deleteBoard(Long id) {
        boardMapper.deleteBoard(id);
    }

    public List<Board> getBoardsByProduct(Long productId,Long categoryId) {
        List<Board> boards = boardMapper.findBoardsByProductId(productId,categoryId);
        return boards;
    }
}
