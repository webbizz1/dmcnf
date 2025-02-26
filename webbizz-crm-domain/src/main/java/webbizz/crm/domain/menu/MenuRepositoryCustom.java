package webbizz.crm.domain.menu;


import org.springframework.data.domain.Page;
import webbizz.crm.domain.menu.dto.MenuDto;
import webbizz.crm.domain.search.dto.SearchBoardDto;
import webbizz.crm.domain.search.dto.SearchCondition;
import webbizz.crm.domain.search.dto.SearchDto;

import java.util.List;
import java.util.Optional;

public interface MenuRepositoryCustom {

    /**
     * 메뉴 시퀀스로 메뉴 조회 (사용자 전용)
     *
     * @param id 메뉴 시퀀스
     * @return 메뉴 DTO (Optional)
     * @author TAEROK HWANG
     */
    Optional<MenuDto> searchByIdForUser(Long id);

    /**
     * groupId 에 해당하고 삭제되지 않은 모든 메뉴 목록을 조회
     *
     * @param groupId 그룹 ID
     * @return 메뉴 DTO 리스트
     * @author TAEROK HWANG
     */
    List<MenuDto> searchAllByGroupId(Long groupId);

    /**
     * 검색어 입력후 검색결과 조회
     *
     * @param text 검색어
     * @return 검색 DTO
     * @author koyeji
     */
    SearchDto searchBySearchText(String text);

    /**
     * 검색어 결과 더보기
     *
     * @param condition 검색어
     * @return 검색 DTO
     * @author koyeji
     */
    Page<SearchBoardDto> searchBoardByCondition( SearchCondition condition);



}
