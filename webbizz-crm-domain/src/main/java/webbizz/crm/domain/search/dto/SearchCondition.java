package webbizz.crm.domain.search.dto;

import lombok.Getter;
import lombok.Setter;
import webbizz.crm.domain.PageCondition;


@Getter
@Setter
public class SearchCondition extends PageCondition  {

    /**
     * 검색어
     */
    private String searchText;

    /**
     * 메뉴시퀀스
     */
    private  Long menuId;



}
