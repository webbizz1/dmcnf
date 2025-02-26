package webbizz.crm.service.survey;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.survey.Survey;
import webbizz.crm.domain.survey.SurveyRepository;
import webbizz.crm.domain.survey.dto.SurveyCondition;
import webbizz.crm.domain.survey.dto.SurveyDto;
import webbizz.crm.domain.survey.dto.SurveySaveDto;
import webbizz.crm.domain.surveyquestion.SurveyQuestion;
import webbizz.crm.domain.surveyquestion.SurveyQuestionRepository;
import webbizz.crm.domain.surveyquestion.dto.SurveyQuestionDto;
import webbizz.crm.domain.surveyquestion.dto.SurveyQuestionSaveDto;
import webbizz.crm.domain.surveyquestionanswer.SurveyQuestionAnswer;
import webbizz.crm.domain.surveyquestionanswer.SurveyQuestionAnswerRepository;
import webbizz.crm.domain.surveyresponsemember.SurveyResponseMemberRepository;
import webbizz.crm.domain.surveyresponsemember.dto.SurveyStep1Dto;
import webbizz.crm.exception.ApiBadRequestException;
import webbizz.crm.exception.ApiNotFoundException;
import webbizz.crm.exception.BusinessException;
import webbizz.crm.util.JwtUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("surveyService")
@RequiredArgsConstructor
public class SurveyServiceImpl extends EgovAbstractServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final SurveyQuestionAnswerRepository questionAnswerRepository;
    private final SurveyResponseMemberRepository surveyResponseMemberRepository;

    private final JwtUtils jwtUtils;

    public Page<SurveyDto> searchAllSurveysForAdmin(final SurveyCondition condition) {
        return surveyRepository.searchAllSurveysForAdmin(condition);
    }

    @Override
    public Page<SurveyDto> searchAllSurveysForUser(final SurveyCondition condition) {
        return surveyRepository.searchAllSurveysForUser(condition);
    }

    @Override
    @Transactional(readOnly = true)
    public ModelAndView surveyResponseModelAndViewForUser(final Long surveyId, final String token) {
        final ModelAndView modelAndView = new ModelAndView();

        if (StringUtils.hasText(token)) {
            final SurveyStep1Dto surveyResponse = this.parseSurveyStep1Token(token);
            final SurveyDto surveyDto = this.findSurveyAndQuestionsAnswersById(surveyId);
            surveyDto.validate();

            modelAndView.addObject("survey", surveyDto);
            modelAndView.addObject("surveyResponse", surveyResponse);
            modelAndView.setViewName("survey/survey_response_step2");
        } else {
            modelAndView.setViewName("survey/survey_response_step1");
        }
        return modelAndView;
    }

    @Override
    @Transactional(readOnly = true)
    public SurveyDto findSurveyAndQuestionsAnswersById(final Long surveyId) {
        final SurveyDto surveyInfo = this.findBySurveyId(surveyId);
        final List<SurveyQuestionDto> questions = this.findAllSurveyQuestionBySurveyId(surveyId);

        return SurveyDto.from(surveyInfo, questions);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countSurveyForActive() {
        return surveyRepository.countSurveyForActive();
    }

    @Override
    @Transactional
    public void saveSurveys(final SurveySaveDto requestDto) {
        requestDto.validate();

        final Survey survey = this.saveSurvey(requestDto);
        final List<SurveyQuestion> questions = this.saveSurveyQuestions(survey, requestDto.getQuestions());
        this.saveSurveyQuestionAnswers(questions, requestDto.getQuestions());
    }

    @Override
    public boolean isSurveyStarted(final Long surveyId) {
        return surveyResponseMemberRepository.existsBySurveyId(surveyId);
    }

    @Override
    public String createSurveyStep1Token(final SurveyStep1Dto dto) {
        return jwtUtils.builder()
                .claim("surveyId", dto.getSurveyId())
                .claim("name", dto.getRespondentInfo().getName())
                .claim("phone", dto.getRespondentInfo().getPhone())
                .claim("email", dto.getRespondentInfo().getEmail())
                .expirationTime(Duration.ofMinutes(123456456)) //TODO: 추후 30분으로 변경 예정
                .build();
    }

    /**
     * 설문조사 참여자 정보 입력하고 나서 step2 단계에서 jwt 토큰 파싱
     *
     * @param token 참여자 정보가 담긴 토큰
     * @return SurveyStep1Dto  step1에서 전달받은 데이터 DTO
     * @author YONGHO LEE
     */
    private SurveyStep1Dto parseSurveyStep1Token(final String token) {
        try {
            final JwtUtils.TokenClaims claims = jwtUtils.parser(token).verify();
            final Claims payload = claims.getPayload();

            final Long surveyId = payload.get("surveyId", Long.class);
            final String name = payload.get("name", String.class);
            final String phone = payload.get("phone", String.class);
            final String email = payload.get("email", String.class);

            return SurveyStep1Dto.fromSurveyResponse(surveyId, name, phone, email);
        } catch (Exception e) {
            throw new BusinessException(404, e.getMessage());
        }
    }

    private SurveyDto findBySurveyId(final Long id) {
        return surveyRepository.findBySurveyId(id)
                .orElseThrow(() -> new BusinessException(404, "해당 설문을 찾을 수 없습니다."));
    }

    private List<SurveyQuestionDto> findAllSurveyQuestionBySurveyId(final Long surveyId) {
        return surveyQuestionRepository.findAllSurveyQuestionBySurveyId(surveyId)
                .orElseThrow(() -> new BusinessException(404, "해당 설문에 질문과 답변을 찾을 수 없습니다."));
    }

    /**
     * 설문 정보, 등록, 수정
     *
     * @author YONGHO LEE
     */
    private Survey saveSurvey(final SurveySaveDto requestDto) {
        final SurveySaveDto.SurveyDto surveyDto = requestDto.getSurvey();
        if (surveyDto.getId() != null) {
            if (this.isSurveyStarted(surveyDto.getId()))
                throw new ApiBadRequestException("설문이 시작되어서 수정이 불가능합니다.");

            return this.surveyUpdate(surveyDto);
        }

        return surveyRepository.save(requestDto.toEntity());
    }

    /**
     * Survey 엔터티는 업데이트, 해당 설문에 해당하는 SurveyQuestion ,SurveyQuestionAnswer 엔티티 모두 삭제
     *
     * @param surveyDto 게시물 등록 DTO
     * @return Survey
     */
    private Survey surveyUpdate(final SurveySaveDto.SurveyDto surveyDto) {
        final Survey survey = surveyRepository.findById(surveyDto.getId())
                .orElseThrow(() -> new ApiNotFoundException(404, "게시물 정보가 존재하지 않습니다."));

        survey.update(
                surveyDto.getTitle(),
                surveyDto.getStartDate(),
                surveyDto.getEndDate(),
                surveyDto.getViewYn()
        );
        // 삭제
        this.deleteSurveyQuestions(survey);

        return surveyRepository.saveAndFlush(survey);
    }

    /**
     * 해당 설문에 해당하는 문항(survey_question),답변(survey_question_answer) 모두 삭제
     *
     * @param survey Survey Entity
     */
    private void deleteSurveyQuestions(final Survey survey) {
        surveyQuestionRepository.deleteAll(survey.getSurveyQuestions());
    }

    /**
     * 해당 설문에 해당하는 문항(survey_question) 저장
     *
     * @param survey       Survey Entity
     * @param questionsDto 설문 문항 정보 저장 DTO 리스트
     */
    private List<SurveyQuestion> saveSurveyQuestions(final Survey survey,
                                                     final List<SurveyQuestionSaveDto> questionsDto
    ) {
        final List<SurveyQuestion> questions = questionsDto.stream()
                .map(dto -> SurveyQuestion.builder()
                        .survey(survey)
                        .title(dto.getTitle())
                        .answerType(dto.getAnswerType())
                        .sortOrder(dto.getSortOrder())
                        .useCustomAnswerYn(dto.getUseCustomAnswerYn())
                        .build())
                .collect(Collectors.toList());

        return surveyQuestionRepository.saveAll(questions);
    }

    /**
     * 해당 설문에 해당하는 문항(survey_question)의 해당하는 답변 저장
     *
     * @param questions    SurveyQuestion Entity 목록
     * @param questionsDto 설문 문항 정보 저장 DTO 리스트
     */
    private void saveSurveyQuestionAnswers(final List<SurveyQuestion> questions,
                                           final List<SurveyQuestionSaveDto> questionsDto
    ) {
        final List<SurveyQuestionAnswer> answers = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            final SurveyQuestion question = questions.get(i);
            final SurveyQuestionSaveDto dto = questionsDto.get(i);

            final List<SurveyQuestionAnswer> questionAnswers = dto.getAnswers().stream()
                    .map(answerDto -> SurveyQuestionAnswer.builder()
                            .surveyQuestion(question)
                            .answerText(answerDto.getText())
                            .sortOrder(answerDto.getSortOrder())
                            .customYn(YN.N)
                            .build())
                    .collect(Collectors.toList());

            answers.addAll(questionAnswers);
        }

        questionAnswerRepository.saveAll(answers);
    }
}
