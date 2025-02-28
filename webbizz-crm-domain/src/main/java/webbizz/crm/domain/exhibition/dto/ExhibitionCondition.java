package webbizz.crm.domain.exhibition.dto;

import lombok.Getter;
import lombok.Setter;
import webbizz.crm.domain.PageCondition;
import webbizz.crm.domain.exhibition.enumset.ExhibitionType;

@Getter
@Setter
public class ExhibitionCondition extends PageCondition {

    private ExhibitionType type;

}
