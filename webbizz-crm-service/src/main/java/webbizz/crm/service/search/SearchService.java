package webbizz.crm.service.search;


import org.springframework.data.domain.Page;
import webbizz.crm.domain.search.dto.SearchBoardDto;
import webbizz.crm.domain.search.dto.SearchCondition;
import webbizz.crm.domain.search.dto.SearchDto;


public interface SearchService {

    /**
     * 메뉴 검색 조회 (사용자용)
     *
     * @param text 검색어
     * @return 검색 DTO
     * @author koyeji
     */
    SearchDto searchBySearchText(final String text);

    /**
     * 메뉴 검색 페이지 조회 (사용자용)
     *
     * @param condition 검색어
     * @return 검색 DTO
     * @author koyeji
     */
    Page<SearchBoardDto> searchBoardByCondition(SearchCondition condition);
}
