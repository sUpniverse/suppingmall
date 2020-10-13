package com.supshop.suppingmall.board;

import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.category.CategoryService;
import com.supshop.suppingmall.image.ImageService;
import com.supshop.suppingmall.mapper.BoardMapper;
import com.supshop.suppingmall.page.Criteria;
import com.supshop.suppingmall.page.ThirtyItemsCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    public int getBoardCount(String type, String searchValue) {
        return boardMapper.findCount(type,searchValue);
    }
    public int getBoardCountByCategoryId(Long categoryId, String type, String searchValue) {
        return boardMapper.findCountByCategoryId(categoryId, type, searchValue);
    }
    public int getBoardCountByUserId(Long userId, String type, String searchValue) {
        return boardMapper.findCountByUserId(userId, type, searchValue);
    }
    public int getBoardsCountByParentCategoryId(Long parentCategoryId, String type, String searchValue) {
        return boardMapper.findCountByParentCategoryId(parentCategoryId, type, searchValue);
    }

    public List<Board> getBoards(Criteria criteria, String type, String searchValue) {
        return boardMapper.findAll(criteria, type, searchValue);
    }
    public List<Board> getBoardsByCategoryId(Criteria criteria, Long categoryId, String type, String searchValue) {
        return boardMapper.findByCategoryId(criteria,categoryId, type, searchValue);
    }
    public List<Board> getBoardByUserId(Criteria criteria,Long userId, String type, String searchValue){
        return boardMapper.findByUserId(criteria, userId, type, searchValue);
    }
    public List<Board> getBoardsByParentCategoryId(Criteria criteria, Long categoryId, String type, String searchValue) {
        return boardMapper.findByParentCategoryId(criteria,categoryId, type, searchValue);
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
        int result = boardMapper.save(board);
        if(imageCount > 0 && result == 1){
            originUrl = setBoardImageUrl(board,urls);
            boardMapper.update(board.getBoardId(), board);
            imageService.saveInStorage(urls,originUrl,board.getBoardId(), boardName);
        }
    }

    // cloud storage의 경로 저장을 위해 이미지 url 변경
    public String setBoardImageUrl(Board board, Set<String> urls) {
        String originUrl;
        String contents = board.getContents();
        String imageUrl = urls.iterator().next();
        String[] splitUrl = imageUrl.split(File.separator);
        int fileIndex = imageUrl.indexOf(splitUrl[splitUrl.length-1]);
        originUrl = imageUrl.substring(0,fileIndex);
        // image/{category}/{yyyyMMdd}/{userId}/fileName => image/{category}/{categoryId}/fileName 로 수정,
        //         image/{category}/{yyyyMMdd}/{userId}가 originUrl에 해당
        contents = contents.replace(originUrl, boardImageUrl+board.getBoardId()+File.separator);
        board.setContents(contents);
        return originUrl;
    }

    @Transactional
    public void updateBoard(Long id, Board board, Set<String> urls) {
        Set<String> newImageUrl = urls.stream().filter(s -> s.split("/").length == 6).collect(Collectors.toSet());
        if(newImageUrl.size() > 0){
            String originUrl= setBoardImageUrl(board,newImageUrl);
            imageService.saveInStorage(newImageUrl,originUrl,board.getBoardId(), boardName);
        }
        boardMapper.update(id, board);
    }

    @Transactional
    public void deleteBoard(Long id) {
        boardMapper.delete(id);
    }



}
