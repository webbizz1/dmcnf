package webbizz.crm.domain.surveyresponsemember.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import webbizz.crm.domain.survey.dto.SurveyDto;

import java.util.List;


@Getter
@RequiredArgsConstructor
public class SurveyResponseResultDto {

    /**
     * 민원 만족도 조사 설문 영역
     */
    private final SurveyDto surveyInfo;

    /**
     * 해당 설문의 질문의 시퀀스 목록
     */
    private final List<Long> questions;

    /**
     * 해당 설문의 응답 목록 Page
     */
    private final Page<SurveyResponseMemberDto> resultList;
}
