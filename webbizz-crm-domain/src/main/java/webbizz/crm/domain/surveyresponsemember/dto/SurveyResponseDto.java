package webbizz.crm.domain.surveyresponsemember.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import webbizz.crm.util.PatternUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SurveyResponseDto {

    /**
     * 설문 시퀀스
     */
    @NotNull(message = "설문은 필수 입니다.")
    private Long surveyId;

    /**
     * 응답자 정보
     */
    @Valid
    private RespondentInfoDto respondentInfo;

    /**
     * 기타 느낀 점이나 의견 (자유 입력)
     */
    private String overallComment;

    /**
     * 응답 상세 문항
     * <p>
     * 빈 리스트인 경우 → @NotEmpty로 검증
     * 리스트 요소 중 null이 있는 경우 → @NotNull로 검증
     * SurveyAnswerDto 내부의 인스턴 → @Valid로 검증
     */
    @NotEmpty(message = "설문 응답을 해주세요.")
    private List<@Valid @NotNull(message = "설문 응답을 해주세요.") SurveyAnswerDto> answers = new ArrayList<>();


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RespondentInfoDto {

        @NotBlank(message = "응답자 이름은 필수입니다.")
        private String name;

        @NotBlank(message = "응답자 전화번호는 필수입니다.")
        @Pattern(regexp = PatternUtils.REGEX_TELEPHONE_NUMBER, message = "핸드폰번호 형식이 올바르지 않습니다.")
        private String phone;

        @NotBlank(message = "응답자 이메일은 필수입니다.")
        @Pattern(regexp = PatternUtils.REGEX_EMAIL, message = "이메일 형식이 올바르지 않습니다.")
        private String email;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SurveyAnswerDto {

        /**
         * 설문 문항 시퀀스
         */
        @NotNull(message = "설문 문항은 필수입니다.")
        private Long questionId;

        /**
         * 설문 문항에 대한 답변 시퀀스
         * 선택한 답변 ID (기타 응답인 경우 null)
         */
        private Long answerId;

        /**
         * 기타 응답 텍스트
         */
        private String customAnswer;

    }

}
