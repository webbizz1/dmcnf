package webbizz.crm.domain.authoritypattern.dto;

import lombok.Getter;
import webbizz.crm.domain.YN;

@Getter
public class AuthorityPatternDto {

    private Long id;

    private Long parentId;

    private String name;

    private String urlPattern;

    private Integer depth;

    private String description;

    private YN manageYn;

}
