package webbizz.crm.domain.search.dto;

import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
public class SearchDto {


    /**
     * 총 건수
     */
    private Long totalCount;

    /**
     * 메뉴 리스트
     */
    private List<SearchMenuDto> menuDtoList;



}
