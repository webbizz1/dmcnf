package webbizz.crm.domain.boardarticle.dto;

import lombok.Getter;
import lombok.Setter;
import webbizz.crm.domain.PageCondition;
import webbizz.crm.domain.YN;

@Getter
@Setter
public class BoardArticleCondition extends PageCondition {

    /**
     * 게시물 시퀀스
     */
    private Long id;

    /**
     * 게시판 시퀀스
     */
    private Long boardId;

    /**
     * 게시판 조회 타입 (메인페이지 > 알림:notice, 뉴스:news)
     */
    private String boardType;

    /**
     * 검색 필드
     */
    private String searchField;

    /**
     * 검색어
     */
    private String searchText;

    /**
     * 노출 여부
     */
    private YN viewYn;

}
