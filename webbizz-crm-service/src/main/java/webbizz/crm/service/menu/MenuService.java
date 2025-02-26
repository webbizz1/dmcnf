package webbizz.crm.service.menu;

import webbizz.crm.domain.menu.dto.MenuDto;
import webbizz.crm.domain.menu.dto.MenuHierarchyDto;
import webbizz.crm.domain.menu.dto.MenuPageDto;
import webbizz.crm.domain.menu.dto.MenuSaveDto;

import java.util.List;

public interface MenuService {

    /**
     * 메뉴 정보 조회 (사용자용)
     *
     * @param id 메뉴 시퀀스
     * @return 메뉴 DTO
     * @author TAEROK HWANG
     */
    MenuDto searchByIdForUser(Long id);

    /**
     * groupId 에 해당하고 삭제되지 않은 모든 메뉴 목록을 조회 (캐시 사용 안함)
     *
     * @param groupId 그룹 ID
     * @return 메뉴 DTO 리스트
     * @author TAEROK HWANG
     * @see #searchAllForNonDeleted(Long, boolean)
     */
    List<MenuDto> searchAllForNonDeleted(Long groupId);

    /**
     * groupId 에 해당하고 삭제되지 않은 모든 메뉴 목록을 조회
     *
     * @param groupId 그룹 ID
     * @param isCache 캐시 사용 여부
     * @return 메뉴 DTO 리스트
     * @author TAEROK HWANG
     */
    List<MenuDto> searchAllForNonDeleted(Long groupId, boolean isCache);

    /**
     * 메뉴에 해당하는 페이지를 출력하기 위한 정보 조회
     *
     * @param id 메뉴 시퀀스
     * @return 부모 메뉴 DTO 리스트
     * @author YONGHO LEE
     */
    MenuPageDto getMenuPage(Long id);

    /**
     * groupId 에 해당하는 메뉴들을 계층 구조로 조회
     *
     * @param groupId 그룹 ID
     * @return 메뉴 계층 구조 DTO 리스트
     * @author TAEROK HWANG
     */
    List<MenuHierarchyDto> getMenuHierarchy(Long groupId);

    void evictMenuCache(Long groupId);

    /**
     * LinkUrl, groupId 에 해당하는 메뉴조회
     *
     * @param url url
     * @param groupId 그룹 ID
     * @return 메뉴 DTO
     * @author YONGHO LEE
     */
    MenuDto findByMenuUrlAndGroupId(String url, Long groupId);

    /**
     * 메뉴 저장
     *
     * @param groupId 그룹 ID
     * @param requestDto 메뉴 저장 요청 DTO 리스트
     * @author TAEROK HWANG
     */
    void saveMenus(Long groupId, List<MenuSaveDto> requestDto);

}
