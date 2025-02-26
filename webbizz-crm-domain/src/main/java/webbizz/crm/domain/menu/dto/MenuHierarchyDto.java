package webbizz.crm.domain.menu.dto;

import lombok.Getter;
import org.springframework.util.StringUtils;
import webbizz.crm.domain.board.dto.InnerBoardDto;
import webbizz.crm.domain.menu.enumset.LinkTarget;
import webbizz.crm.domain.menu.enumset.MenuType;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MenuHierarchyDto {

    private final Long id;

    private final Long parentId;

    private final Integer depth;

    private final Integer sortOrder;

    private final String name;

    private final MenuType type;

    private final InnerBoardDto board;

    private final String linkUrl;

    private final LinkTarget linkTarget;

    private final List<MenuHierarchyDto> children = new ArrayList<>();

    public MenuHierarchyDto(MenuDto menuDto) {
        this.id = menuDto.getId();
        this.parentId = menuDto.getParentId();
        this.depth = menuDto.getDepth();
        this.sortOrder = menuDto.getSortOrder();
        this.name = menuDto.getName();
        this.type = menuDto.getType();
        this.board = menuDto.getBoard();
        this.linkUrl = menuDto.getLinkUrl();
        this.linkTarget = menuDto.getLinkTarget();
    }

    /**
     * 해당 메뉴의 링크 설정 메뉴타입이 링크 일시 링크 URL 리턴
     *
     * @return url 경로
     */
    public String getLink() {
        return this.type == MenuType.LINK && StringUtils.hasText(this.linkUrl) ? this.linkUrl : "/pages/" + this.id;
    }

    /**
     * 모바일 aside 에서 사용 -> 메뉴타입이 링크 일시 링크 URL 리턴
     *
     * @return url 경로 및 #
     */
    public String getAsideLink() {
        return !this.hasChildren() ? getLink() : "#";
    }

    /**
     * href 타겟의 링크 속성을 설정
     * _blank 링크된 문서를 새로운 윈도우나 탭(tab)에서 오픈함.
     * _self 링크된 문서를 링크가 위치한 현재 프레임에서 오픈함.
     * null or '' 기본값으로 생략 가능.
     *
     * @return url 경로
     * @link https://www.tcpschool.com/html-tag-attrs/a-target
     */
    public String getTarget() {
        return this.type == MenuType.LINK && this.linkTarget != null
                ? "_" + this.linkTarget.getCode().toLowerCase()
                : "";
    }

    /**
     * a 태그의 active 클래스를 주기위함.(현재 메뉴의 상위 메뉴 까지 클래스 추가용도)
     *
     * @return active 클래스명 또는 ""
     */
    public String getActiveClass(final MenuPageDto menuPage) {
        return menuPage.isInPath(this.id) ? "active" : "";
    }

    /**
     * 현재 메뉴의 자식 메뉴 존재 여부 확인
     *
     * @return boolean
     */
    public boolean hasChildren() {
        return !children.isEmpty();
    }
}
