package com.supshop.suppingmall.board;

import com.supshop.suppingmall.category.CategoryService;
import com.supshop.suppingmall.common.Search;
import com.supshop.suppingmall.image.ImageService;
import com.supshop.suppingmall.mapper.BoardMapper;
import com.supshop.suppingmall.page.Criteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
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
    private final ImageService imageService;

    private static final String boardName = "board";
    private static final String boardImageUrl = "/images/board/";

    public int getBoardCount(Search search) {
        return boardMapper.findCount(search);
    }
    public int getBoardCountByCategoryId(Long categoryId, Search search) {
        return boardMapper.findCountByCategoryId(categoryId, search);
    }
    public int getBoardCountByUserId(Long userId, Search searche) {
        return boardMapper.findCountByUserId(userId, searche);
    }
    public int getBoardsCountByParentCategoryId(Long parentCategoryId, Search search) {
        return boardMapper.findCountByParentCategoryId(parentCategoryId, search);
    }

    public List<Board> getBoards(Criteria criteria, Search search) {
        return boardMapper.findAll(criteria, search);
    }
    public List<Board> getBoardsByCategoryId(Criteria criteria, Long categoryId, Search search) {
        return boardMapper.findByCategoryId(criteria,categoryId, search);
    }
    public List<Board> getBoardByUserId(Criteria criteria,Long userId, Search search){
        return boardMapper.findByUserId(criteria, userId, search);
    }
    public List<Board> getBoardsByParentCategoryId(Criteria criteria, Long categoryId, Search search) {
        return boardMapper.findByParentCategoryId(criteria,categoryId, search);
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

    /**
     * 게시물 저장
     * @param board
     * @param urls 새로 추가된 이미지 경로들 모음
     */
    @Transactional
    public void createBoard(Board board, Set<String> urls) throws FileNotFoundException {
        String originUrl = null;
        int result = boardMapper.save(board);
        if(urls!=null && urls.size() > 0 && result == 1){
            originUrl = setBoardImageUrl(board,urls);
            boardMapper.update(board.getBoardId(), board);
            boolean isSaved = imageService.saveInStorage(urls, originUrl, board.getBoardId(), boardName);
            if(!isSaved) {
                // Todo : 파일 저장 실패 exception && 시간초과 (지금은 단지 3번 실패 후 exception)
                int count  = 0;
                while (!isSaved && count < 3){
                    isSaved = imageService.saveInStorage(urls, originUrl, board.getBoardId(), boardName);
                    count++;
                }

                if(!isSaved) throw new FileNotFoundException();
            }
        }
    }

    /**
     * cloud storage의 경로 저장을 위해 이미지 url 변경
     * @param board
     * @param urls 새로 추가된 이미지 경로들 모음
     * @return
     */
    public String setBoardImageUrl(Board board, Set<String> urls) {
        String contents = board.getContents();

        String imageUrl = urls.iterator().next();   // 임시 저장된 경로 하나를 가져옴
        String[] splitUrl = imageUrl.split(File.separator);
        int fileIndex = imageUrl.indexOf(splitUrl[splitUrl.length-1]);   // fileName을 제외한 경로의 index

        // originUrl => image/{category}/{yyyyMMdd}/{userId}
        String originUrl = imageUrl.substring(0, fileIndex);

        // image/{category}/{yyyyMMdd}/{userId}/fileName => image/{category}/{board or product Id}/fileName 로 모든 경로 수정,
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

    @Transactional
    public void blindBoard(Long id) {
        boardMapper.blind(id);
    }
}
