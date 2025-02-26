package webbizz.crm.domain.menu.dto;

import lombok.Getter;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.board.dto.InnerBoardSaveDto;
import webbizz.crm.domain.menu.enumset.LinkTarget;
import webbizz.crm.domain.menu.enumset.MenuType;

@Getter
public class MenuSaveDto {

    private Long id;

    private Long parentId;

    private Integer depth;

    private Integer sortOrder;

    private String name;

    private MenuType type;

    private InnerBoardSaveDto board;

    private String content;

    private String linkUrl;

    private LinkTarget linkTarget;

    private String seoKeyword;

    private String seoDescription;

    private String managerDepartment;

    private String managerTelephone;

    private String managerEmail;

    private YN publicUseYn;

    private YN viewYn;

}
