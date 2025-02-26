package webbizz.crm.domain.boardarticle;

import org.springframework.data.domain.Page;
import webbizz.crm.domain.boardarticle.dto.BoardArticleCondition;
import webbizz.crm.domain.boardarticle.dto.BoardArticleDto;
import webbizz.crm.domain.boardarticle.dto.BoardArticleListDto;

import java.util.List;
import java.util.Optional;

public interface BoardArticleRepositoryCustom {

    /**
     * 게시물 시퀀스로 게시물 정보 조회
     *
     * @param id 게시물 시퀀스
     * @return 게시물 DTO (Optional)
     * @author TAEROK HWANG
     */
    Optional<BoardArticleDto> searchById(Long id);

    /**
     * 게시판 시퀀스와 게시물 시퀀스로 게시물 정보 조회
     *
     * @param boardId 게시판 시퀀스
     * @param id 게시물 시퀀스
     * @return 게시물 DTO (Optional)
     * @author TAEROK HWANG
     */
    Optional<BoardArticleDto> searchBy(Long boardId, Long id);

    /**
     * 검색 조건으로 게시물 목록 조회
     *
     * @param condition 검색 조건
     * @return 게시물 DTO 리스트 (Page)
     * @author TAEROK HWANG
     */
    Page<BoardArticleDto> searchAllByCondition(BoardArticleCondition condition);

    /**
     * 회원이 최근 작성한 게시물 조회
     *
     * @param memberId 회원 시퀀스
     * @param count 조회할 수
     * @return 게시물 DTO 리스트
     * @author TAEROK HWANG
     */
    List<BoardArticleDto> searchRecentBy(Long memberId, int count);

    /**
     * 전체 게시물에서 최근 작성된 게시물 조회
     *
     * @param count 조회할 수
     * @return 게시물 DTO 리스트
     * @author TAEROK HWANG
     */
    List<BoardArticleDto> searchRecentByCount(int count);

    /**
     * 최근 공지사항 게시물 조회
     *
     * @return 게시물 DTO (Optional)
     * @author TAEROK HWANG
     */
    Optional<BoardArticleDto> searchRecentNotice();

    /**
     * 게시물 조회 수 증가
     *
     * @param boardId 게시판 시퀀스
     * @param id 게시물 시퀀스
     * @author TAEROK HWANG
     */
    void addReadCount(Long boardId, Long id);

    /**
     * 타입별 게시물 목록 조회
     * @param condition 검색조건
     * @return 게시물 DTO 리스트
     * @author hyeonji
     */
    List<BoardArticleListDto> searchAllByBoardType(BoardArticleCondition condition);
}
