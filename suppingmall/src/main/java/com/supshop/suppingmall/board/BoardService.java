package com.supshop.suppingmall.board;

import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.category.CategoryService;
import com.supshop.suppingmall.image.ImageService;
import com.supshop.suppingmall.mapper.BoardMapper;
import com.supshop.suppingmall.page.Criteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardMapper boardMapper;
    private final CategoryService categoryService;
    private final ImageService imageService;

    private static final String boardName = "board";
    private static final String boardImageUrl = "/images/board/";

    public List<Board> getBoards(Criteria boardCriteria, Long categoryId, String type, String searchValue) {
        if(categoryId == null) {
            Category categoryByEnName = categoryService.getCategoryByEnName(boardName);
            return boardMapper.findAll(boardCriteria,categoryByEnName.getId(), type, searchValue);
        }
        return boardMapper.findAll(boardCriteria,categoryId, type, searchValue);
    }

    public int getBoardCount(Long categoryId, String type, String searchValue) {
        return boardMapper.selectBoardCount(categoryId,type,searchValue);
    }

    @Transactional
    public Board getBoard(Long id) {
        Optional<Board> board = boardMapper.findOne(id);
        if(!board.isEmpty()) {
            boardMapper.updateBoardHit(id);
            return board.get();
        }
        return null;
    }

    @Transactional
    public void createBoard(Board board, Set<String> urls) {
        int imageCount = urls.size();
        String originUrl = null;
        int result = boardMapper.insertBoard(board);
        if(imageCount > 0 && result == 1){
            originUrl = setBoardImageUrl(board,urls);
            boardMapper.updateBoard(board.getBoardId(), board);
            imageService.saveInStorage(urls,originUrl,board.getBoardId(), boardName);
        }
    }

    // image/{category}/{yyyyMMdd}/{userId}/fileName => image/{category}/{categoryId}/fileName
    // cloud storage의 경로 저장을 위해 이미지 url 변경
    public String setBoardImageUrl(Board board, Set<String> urls) {
        String originUrl;
        String contents = board.getContents();
        String imageUrl = urls.iterator().next();
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

}
