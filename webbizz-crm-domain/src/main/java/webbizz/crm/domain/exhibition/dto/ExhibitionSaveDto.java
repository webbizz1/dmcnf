package webbizz.crm.domain.exhibition.dto;

import lombok.Getter;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.exhibition.enumset.BannerType;
import webbizz.crm.domain.exhibition.enumset.ExhibitionType;
import webbizz.crm.domain.exhibition.enumset.LinkTarget;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
public class ExhibitionSaveDto {

    private Long id;

    @NotNull(message = "전시 유형 정보가 없습니다.")
    private ExhibitionType type;

    private BannerType bannerType;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    private String linkUrl;

    @NotNull(message = "링크 유형을 선택해 주세요.")
    private LinkTarget linkTarget;

    private String pcImageUUID;

    private String mobileImageUUID;

    @NotNull(message = "노출 시작일을 입력해 주세요.")
    private LocalDate viewStartDate;

    private LocalDate viewEndDate;

    private YN viewYn;

}
