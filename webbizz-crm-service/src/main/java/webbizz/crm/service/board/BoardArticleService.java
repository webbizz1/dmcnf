package webbizz.crm.service.board;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import webbizz.crm.domain.boardarticle.dto.BoardArticleCondition;
import webbizz.crm.domain.boardarticle.dto.BoardArticleDto;
import webbizz.crm.domain.boardarticle.dto.BoardArticleListDto;
import webbizz.crm.domain.boardarticle.dto.BoardArticleSaveDto;
import webbizz.crm.domain.member.entity.MemberUserDetails;

import java.util.List;

public interface BoardArticleService {

    /**
     * 게시물 시퀀스로 게시물 정보 조회
     *
     * @param id 게시물 시퀀스
     * @return 게시물 DTO
     * @author TAEROK HWANG
     */
    BoardArticleDto searchById(Long id);

    /**
     * 게시판 시퀀스와 게시물 시퀀스로 게시물 정보 조회 (엄격 조회)
     *
     * @param boardId 게시판 시퀀스
     * @param id 게시물 시퀀스
     * @return 게시물 DTO
     * @author TAEROK HWANG
     */
    BoardArticleDto searchBy(Long boardId, Long id);

    /**
     * 게시판 시퀀스와 게시물 시퀀스로 게시물 정보 조회 (엄격 조회 및 조회 수 증가)
     *
     * @param boardId 게시판 시퀀스
     * @param id 게시물 시퀀스
     * @return 게시물 DTO
     */
    BoardArticleDto searchByWithReadCount(Long boardId, Long id);

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
     * @return 게시물 DTO (없으면 null)
     * @author TAEROK HWANG
     */
    BoardArticleDto searchRecentNotice();

    /**
     * 게시물 등록·수정
     *
     * @param memberUserDetails 사용자 정보
     * @param requestDto 게시물 저장 요청 DTO
     * @param httpMethod HTTP 메소드
     * @return 게시물 시퀀스
     * @author TAEROK HWANG
     */
    Long saveArticle(MemberUserDetails memberUserDetails, BoardArticleSaveDto requestDto, HttpMethod httpMethod);

    /**
     * 게시물 삭제
     *
     * @param boardId 게시판 시퀀스
     * @param id 게시물 시퀀스
     * @author TAEROK HWANG
     */
    void deleteArticle(Long boardId, Long id);

    /**
     * 타입별 게시물 조회(메인페이지 조회)
     * @param condition 검색 조건
     */
    List<BoardArticleListDto> searchAllByBoardType(BoardArticleCondition condition);
}
