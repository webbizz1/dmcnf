package webbizz.crm.service.search;

import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webbizz.crm.domain.menu.MenuRepository;
import webbizz.crm.domain.search.dto.SearchBoardDto;
import webbizz.crm.domain.search.dto.SearchCondition;
import webbizz.crm.domain.search.dto.SearchDto;


@Service("searchService")
@RequiredArgsConstructor
public class SearchServiceImpl extends EgovAbstractServiceImpl implements SearchService {

    private final MenuRepository menuRepository;




    /**
     * SearchDto
     *
     * @param text 검색어
     * @return 검색결과 SearchDto
     * @author koyeji
     */
    @Transactional(readOnly = true)
    public SearchDto searchBySearchText(final String text) {
        return menuRepository.searchBySearchText(text);


    }
    /**
     * SearchDto
     *
     * @param condition 검색어
     * @return 검색결과 SearchDto
     * @author koyeji
     */
    @Transactional(readOnly = true)
    @Override
    public Page<SearchBoardDto> searchBoardByCondition(SearchCondition condition) {
        return menuRepository.searchBoardByCondition(condition);
    }


}
