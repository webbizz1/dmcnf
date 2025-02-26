package webbizz.crm.domain.terms.dto;

import lombok.Getter;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.terms.enumset.TermsType;

import java.time.LocalDateTime;

@Getter
public class TermsDto {

    private Long id;

    private TermsType type;

    private String content;

    private YN activeYn;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
