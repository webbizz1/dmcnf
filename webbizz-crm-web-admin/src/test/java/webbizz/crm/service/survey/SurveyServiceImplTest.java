package webbizz.crm.service.survey;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.survey.SurveyRepository;
import webbizz.crm.domain.survey.dto.SurveyCondition;
import webbizz.crm.domain.survey.dto.SurveyDto;
import webbizz.crm.domain.survey.dto.SurveyListDto;
import webbizz.crm.domain.survey.dto.SurveySaveDto;
import webbizz.crm.domain.survey.enumset.AnswerType;
import webbizz.crm.domain.surveyquestion.SurveyQuestionRepository;
import webbizz.crm.domain.surveyquestion.dto.SurveyQuestionSaveDto;
import webbizz.crm.domain.surveyquestionanswer.dto.SurveyQuestionAnswerSaveDto;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SurveyServiceImplTest {

    @Autowired
    private SurveyService surveyService;
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private SurveyQuestionRepository surveyQuestionRepository;

    @Test
    @DisplayName("findSurveyAndQuestionsAnswersById")
    void  findSurveyAndQuestionsAnswersById() throws Exception {
        surveyService.findSurveyAndQuestionsAnswersById(1L);
    }

    @Test
    @DisplayName("findBySurveyId")
    void findBySurveyId() throws Exception {
        // given
        final Long id = 1L;

        // when
        final SurveyDto.Survey bySurveyId = surveyRepository.findBySurveyId(id).orElse(null);

        // then
        assertThat(bySurveyId.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("findAllSurveyQuestionBySurveyId")
    void findAllSurveyQuestionBySurveyId() throws Exception {
        surveyQuestionRepository.findAllSurveyQuestionBySurveyId(1L);
    }

    @Test
    @DisplayName("test")
    void test() throws Exception {
        final Page<SurveyListDto> pages = surveyService.searchAllSurveys(new SurveyCondition(""));
        pages.getContent().forEach(s -> System.out.println("s = " + s.getStatus()));
    }

    @Test
    @DisplayName("save")
    void save() throws Exception {
        surveyService.saveSurveys(getSurveySaveDto());
    }

    @Test
    @DisplayName("v2")
    void v2() throws Exception {
        // given
        final SurveySaveDto survey = getSurveySaveDto();
        final List<SurveyQuestionSaveDto> questions = questionsOK();

        // when && then
        surveyService.saveSurveys(new SurveySaveDto(survey.getSurvey(), questions));

    }

    private SurveySaveDto getSurveySaveDto() {
        return new SurveySaveDto(
                new SurveySaveDto.SurveyDto(
                        "제목입니다.1234",
                        LocalDate.now(),
                        LocalDate.now().plusDays(123),
                        YN.Y));
    }

    private List<SurveyQuestionSaveDto> questionsOK() {
        final SurveyQuestionAnswerSaveDto answer1 = new SurveyQuestionAnswerSaveDto(-3L, "문항의 답변 1", 1);
        final SurveyQuestionAnswerSaveDto answer2 = new SurveyQuestionAnswerSaveDto(-4L, "문항의 답변 2", 2);
        final SurveyQuestionAnswerSaveDto answer3 = new SurveyQuestionAnswerSaveDto(-5L, "문항의 답변 3", 3);

        final webbizz.crm.domain.surveyquestion.dto.SurveyQuestionSaveDto question = new webbizz.crm.domain.surveyquestion.dto.SurveyQuestionSaveDto(
                null,
                "문항입니다",
                AnswerType.CHECKBOX,
                1,
                YN.N,
                Arrays.asList(answer1, answer2, answer3));

        return Collections.singletonList(question);
    }

}