package webbizz.crm.domain.board.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import webbizz.crm.domain.PageCondition;
import webbizz.crm.domain.YN;
import webbizz.crm.util.PatternUtils;

import java.time.LocalDate;

@Getter
@Setter
public class BoardCondition extends PageCondition {

    /**
     * 노출 상태
     */
    private YN viewYn;

    /**
     * 등록일 - 시작일
     */
    @DateTimeFormat(pattern = PatternUtils.DATE_FORMAT)
    private LocalDate startDate;

    /**
     * 등록일 - 종료일
     */
    @DateTimeFormat(pattern = PatternUtils.DATE_FORMAT)
    private LocalDate endDate;

}
