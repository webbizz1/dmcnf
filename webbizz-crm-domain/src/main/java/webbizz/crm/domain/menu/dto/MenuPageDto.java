package webbizz.crm.domain.menu.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MenuPageDto {

    /**
     * 메뉴 경로 문자열 (예: A > B > C)
     */
    private final String navigator;

    /**
     * 현재 선택한 메뉴
     */
    private final MenuDto menu;

    /**
     * 현재 선택한 메뉴 및 상위 메뉴 리스트 (최상위 메뉴가 우선)
     */
    private final List<MenuDto> parents;

    /**
     * 현재 선택한 메뉴의 형제 메뉴 리스트
     */
    private final List<MenuDto> brothers;

    /**
     * 현재 선택한 메뉴의 브레드크럼 출력을 위한 정보가 담긴 메뉴 리스트
     */
    private final List<List<MenuDto>> menuBreadcrumbs;

    /**
     * 현재 선택한 메뉴와 상위 메뉴의 ID 리스트
     */
    private final List<Long> selectedIds;

    public MenuPageDto() {
        this.navigator = null;
        this.menu = new MenuDto();
        this.parents = new ArrayList<>();
        this.brothers = new ArrayList<>();
        this.menuBreadcrumbs = new ArrayList<>();
        this.selectedIds = new ArrayList<>();
    }

    public MenuPageDto(final MenuDto menuDto,
                       final List<MenuDto> menus,
                       final List<MenuDto> brothers,
                       final List<List<MenuDto>> menuBreadcrumbs) {

        this.menu = menuDto;
        this.parents = menus;
        this.brothers = brothers;
        this.menuBreadcrumbs = menuBreadcrumbs;

        this.navigator = menus.stream().map(MenuDto::getName).collect(Collectors.joining(" > "));
        this.selectedIds = menus.stream().map(MenuDto::getId).collect(Collectors.toList());
    }

    /**
     * menuId가 부모에 속해있는지 확인
     *
     * @param menuId 해당 메뉴의 시퀀스
     * @return boolean
     */
    public boolean isInPath(final Long menuId) {
        return selectedIds.contains(menuId);
    }

    /**
     * 현재 활성화된 메뉴인지 확인하는 메서드
     */
    public boolean isCurrentMenu(final Long menuId) {
        return menu != null && menu.getId().equals(menuId);
    }

}
