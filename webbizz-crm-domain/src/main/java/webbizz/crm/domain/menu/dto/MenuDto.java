package webbizz.crm.domain.menu.dto;

import lombok.Getter;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.board.dto.InnerBoardDto;
import webbizz.crm.domain.menu.enumset.LinkTarget;
import webbizz.crm.domain.menu.enumset.MenuType;

@Getter
public class MenuDto {

    /**
     * 메뉴 시퀀스
     */
    private Long id;

    /**
     * 상위 메뉴 시퀀스 (NULL: 최상위 메뉴)
     */
    private Long parentId;

    /**
     * 그룹 시퀀스
     */
    private Long groupId;

    /**
     * 메뉴 깊이 (1부터 시작)
     */
    private Integer depth;

    /**
     * 정렬 순서
     */
    private Integer sortOrder;

    /**
     * 메뉴 이름
     */
    private String name;

    /**
     * 메뉴 유형 [LINK 링크 | CONTENT 콘텐츠 | BOARD 게시판]
     */
    private MenuType type;

    /**
     * 게시판 시퀀스 (메뉴 유형 BOARD 전용)
     */
    private InnerBoardDto board;

    /**
     * 콘텐츠 (메뉴 유형 CONTENT 전용)
     */
    private String content;

    /**
     * 링크 URL (메뉴 유형 LINK 전용)
     */
    private String linkUrl;

    /**
     * 링크 타겟 (메뉴 유형 LINK 전용) [SELF 현재 창 | BLANK 새 창]
     */
    private LinkTarget linkTarget;

    /**
     * SEO 최적화 키워드
     */
    private String seoKeyword;

    /**
     * SEO 최적화 설명
     */
    private String seoDescription;

    /**
     * 담당자 담당부서
     */
    private String managerDepartment;

    /**
     * 담당자 연락처
     */
    private String managerTelephone;

    /**
     * 담당자 이메일
     */
    private String managerEmail;

    /**
     * 공공저작물 자유 이용 여부
     */
    private YN publicUseYn;

    /**
     * 노출 여부
     */
    private YN viewYn;

}
