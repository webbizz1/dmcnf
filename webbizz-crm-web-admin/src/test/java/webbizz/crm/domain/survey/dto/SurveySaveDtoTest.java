package webbizz.crm.domain.survey.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.survey.enumset.AnswerType;
import webbizz.crm.domain.surveyquestion.dto.SurveyQuestionSaveDto;
import webbizz.crm.domain.surveyquestionanswer.dto.SurveyQuestionAnswerSaveDto;
import webbizz.crm.exception.ApiBadRequestException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SurveySaveDtoTest {

    ValidatorFactory factory;
    Validator validator;

    @BeforeEach
    public void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("validate survey")
    void validateSurvey() throws Exception {
        final SurveySaveDto surveySaveDto = new SurveySaveDto(
                new SurveySaveDto.SurveyDto(
                        1L,
                        "제목입니다.",
                        null,
                        LocalDate.now().plusDays(123),
                        YN.Y));

        final Set<ConstraintViolation<SurveySaveDto>> validate = validator.validate(surveySaveDto);
        validate.forEach(System.out::println);

        assertThat(validate).isNotEmpty();
    }


    @Test
    @DisplayName("validateDates")
    void validateDates() throws Exception {
        // given
        final SurveySaveDto.SurveyDto survey =
                new SurveySaveDto.SurveyDto(1L,
                        "1a2a",
                        LocalDate.now(),
                        LocalDate.now().minusDays(123),
                        YN.Y);
        // when & then
        assertThrows(ApiBadRequestException.class, () -> new SurveySaveDto(survey).getSurvey().validateDates());
    }

    @Test
    @DisplayName("validateSurveyQuestionsOK")
    void validateSurveyQuestionsOK() throws Exception {
        // given
        final List<SurveyQuestionSaveDto> questions = this.questionsOK();

        // when
        final Set<ConstraintViolation<List<SurveyQuestionSaveDto>>> validate =
                validator.validate(questions);

        // then
        assertThat(validate).isEmpty();
    }

    @Test
    @DisplayName("validateSurveyQuestionsFail")
    void validateSurveyQuestionsFail() throws Exception {
        // given
        final List<SurveyQuestionSaveDto> questions = this.questionsFail();
        final SurveySaveDto requestDto = new SurveySaveDto(getSurveySaveDto().getSurvey(), questions);

        // when
        final Set<ConstraintViolation<SurveySaveDto>> validate = validator.validate(requestDto);

        // then
        validate.forEach(System.out::println);
        assertThat(validate).isNotEmpty();
    }

    @Test
    @DisplayName("duplicatedV1")
    void duplicatedV1() throws Exception {
        final List<String> list = asList("A", "B", "B", "C", "D", "D");

        list.stream()
                .filter(i -> Collections.frequency(list, i) > 1) // 메서드는 각 요소를 순회하여 요소의 개수를 반환한다.
                .collect(Collectors.toSet())
                .forEach(System.out::println);
    }

    @Test
    @DisplayName("duplicatedQuestionAnswer")
    void duplicatedQuestionAnswer() throws Exception {
        // given
        final List<SurveyQuestionSaveDto> questions = this.questionsFail();
        final SurveySaveDto requestDto = new SurveySaveDto(getSurveySaveDto().getSurvey(), questions);

        // when && then
        assertThrows(ApiBadRequestException.class, requestDto::validate);
    }

    @Test
    @DisplayName("duplicatedV2")
    void duplicatedV2() throws Exception {
        final List<Student> list = asList
                (
                        new Student("이용호", 1),
                        new Student("이용호", 2),
                        new Student("1a", 3),
                        new Student("2a", 4),
                        new Student("3a", 5)
                );

        list.stream().map(Student::getName).collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting()
        )).forEach((a, b) -> {
            System.out.println("a = " + a); // 이용호 , 1a , 2a , 3a
            System.out.println("b = " + b); // 카운팅갯수
        });
    }


    private SurveySaveDto getSurveySaveDto() {
        return new SurveySaveDto(
                new SurveySaveDto.SurveyDto(
                        1L,
                        "제목입니다.",
                        LocalDate.now(),
                        LocalDate.now().plusDays(123),
                        YN.Y));
    }

    private List<SurveyQuestionSaveDto> questionsOK() {
        final SurveyQuestionAnswerSaveDto answer1 = new SurveyQuestionAnswerSaveDto(-3L, "문항의 답변 1", 1);
        final SurveyQuestionAnswerSaveDto answer2 = new SurveyQuestionAnswerSaveDto(-4L, "문항의 답변 2", 2);
        final SurveyQuestionAnswerSaveDto answer3 = new SurveyQuestionAnswerSaveDto(-5L, "문항의 답변 3", 3);

        final SurveyQuestionSaveDto question = new SurveyQuestionSaveDto(2L, "문항입니다", AnswerType.CHECKBOX, 1, YN.N,
                Arrays.asList(answer1, answer2, answer3));

        return Collections.singletonList(question);
    }

    private List<SurveyQuestionSaveDto> questionsFail() {
        final SurveyQuestionAnswerSaveDto answer1 = new SurveyQuestionAnswerSaveDto(-3L, "문항의 답변 2", 1);
        final SurveyQuestionAnswerSaveDto answer2 = new SurveyQuestionAnswerSaveDto(-4L, "문항의 답변 2", 2);
        final SurveyQuestionAnswerSaveDto answer3 = new SurveyQuestionAnswerSaveDto(-5L, "문항의 답변 3", 3);

        final SurveyQuestionSaveDto question = new SurveyQuestionSaveDto(2L, "", AnswerType.CHECKBOX, 1, YN.N,
                asList(answer1, answer2, answer3));

        return Collections.singletonList(question);
    }

    static class Student {
        private String name;
        private int age;

        public Student(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }
    }
}