package webbizz.crm.domain.surveyresponsemember.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SurveyStep1Dto {

    private final Long surveyId;

    private final SurveyResponseDto.RespondentInfoDto respondentInfo;

    public static SurveyStep1Dto fromSurveyResponse(final Long surveyId,
                                                    final String name,
                                                    final String phone,
                                                    final String email
    ) {
        return new SurveyStep1Dto(surveyId, new SurveyResponseDto.RespondentInfoDto(name, phone, email));
    }

}
