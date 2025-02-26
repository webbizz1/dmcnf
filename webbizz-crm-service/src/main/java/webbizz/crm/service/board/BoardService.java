package webbizz.crm.service.board;

import org.springframework.data.domain.Page;
import webbizz.crm.domain.board.dto.BoardCondition;
import webbizz.crm.domain.board.dto.BoardDto;
import webbizz.crm.domain.board.dto.BoardSaveDto;

import java.util.List;

public interface BoardService {

    /**
     * 게시판 목록 조회 (간략 정보) <br />
     * 캐시 데이터를 조회한 뒤 있으면 반환, 없으면 조회하여 캐시에 저장 후 반환
     *
     * @return 게시판 간략 정보 DTO 리스트
     */
    List<BoardDto> getBoardCompactList();

    /**
     * 게시판 시퀀스로 게시판 정보 조회 (게시판 설정 값 포함)
     *
     * @param id 게시판 시퀀스
     * @return 게시판 DTO
     * @author TAEROK HWANG
     */
    BoardDto searchById(Long id);

    /**
     * 검색 조건으로 게시판 목록 조회 (게시판 설정 값 포함)
     *
     * @param condition 검색 조건
     * @return 게시판 DTO 리스트 (Page)
     * @author TAEROK HWANG
     */
    Page<BoardDto> searchAllByCondition(BoardCondition condition);

    /**
     * 게시판 정보 생성
     *
     * @param requestDto 게시판 생성 요청 DTO
     * @author TAEROK HWANG
     */
    Long createBoard(BoardSaveDto requestDto);

    /**
     * 게시판 정보 수정
     *
     * @param requestDto 게시판 수정 요청 DTO
     * @author TAEROK HWANG
     */
    Long updateBoard(BoardSaveDto requestDto);

}
