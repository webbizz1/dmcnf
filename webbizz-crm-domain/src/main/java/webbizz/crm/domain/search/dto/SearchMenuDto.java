package webbizz.crm.domain.search.dto;

import lombok.Getter;
import lombok.Setter;
import webbizz.crm.domain.menu.dto.MenuPageDto;

import java.util.List;

@Getter
@Setter
public class SearchMenuDto {

    /**
     * 메뉴 시퀀스
     */
    private Long id;

    /**
     * 메뉴 정보
     */
    private MenuPageDto menuPageDto;

    /**
     * 메뉴명
     */
    private String name;

    /**
     * 검색건수
     */
    private Long menuCount;


    /**
     * 검색건수
     */
    private List<SearchBoardDto> searchBoardDtoList;


}
