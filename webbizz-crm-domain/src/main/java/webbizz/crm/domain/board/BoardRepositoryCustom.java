package webbizz.crm.domain.board;

import org.springframework.data.domain.Page;
import webbizz.crm.domain.board.dto.BoardCondition;
import webbizz.crm.domain.board.dto.BoardDto;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {

    /**
     * 게시판 시퀀스로 게시판 정보 조회
     *
     * @param id 게시판 시퀀스
     * @return 게시판 DTO (Optional)
     * @author TAEROK HWANG
     */
    Optional<BoardDto> searchById(Long id);

    /**
     * 검색 조건으로 게시판 목록 조회
     *
     * @param condition 검색 조건
     * @return 게시판 DTO 리스트 (Page)
     * @author TAEROK HWANG
     */
    Page<BoardDto> searchAllByCondition(BoardCondition condition);

    /**
     * 사용중인 게시판 목록 간략 조회 (id, name) <br />
     * 게시판 이름 순으로 정렬됨
     *
     * @return 게시판 리스트
     * @author TAEROK HWANG
     */
    List<BoardDto> searchAllCompact();

}
