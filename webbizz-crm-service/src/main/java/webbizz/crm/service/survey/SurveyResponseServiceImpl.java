package webbizz.crm.service.survey;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webbizz.crm.domain.survey.Survey;
import webbizz.crm.domain.survey.SurveyRepository;
import webbizz.crm.domain.survey.dto.SurveyDto;
import webbizz.crm.domain.surveyquestion.SurveyQuestion;
import webbizz.crm.domain.surveyquestion.SurveyQuestionRepository;
import webbizz.crm.domain.surveyquestionanswer.SurveyQuestionAnswer;
import webbizz.crm.domain.surveyquestionanswer.SurveyQuestionAnswerRepository;
import webbizz.crm.domain.surveyresponsedetail.SurveyResponseDetailRepository;
import webbizz.crm.domain.surveyresponsedetail.dto.SurveyAnswerStatisticsDto;
import webbizz.crm.domain.surveyresponsedetail.dto.SurveyResponseDetailDto;
import webbizz.crm.domain.surveyresponsemember.SurveyResponseMember;
import webbizz.crm.domain.surveyresponsemember.SurveyResponseMemberRepository;
import webbizz.crm.domain.surveyresponsemember.dto.SurveyOverallCommentDto;
import webbizz.crm.domain.surveyresponsemember.dto.SurveyResponseDto;
import webbizz.crm.domain.surveyresponsemember.dto.SurveyResponseMemberDto;
import webbizz.crm.domain.surveyresponsemember.dto.SurveyResponseResultDto;
import webbizz.crm.exception.ApiNotFoundException;
import webbizz.crm.util.RequestUtils;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.springframework.util.StringUtils.hasText;

@Service("surveyResponseService")
@RequiredArgsConstructor
public class SurveyResponseServiceImpl extends EgovAbstractServiceImpl implements SurveyResponseService {

    private final SurveyRepository surveyRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final SurveyQuestionAnswerRepository questionAnswerRepository;
    private final SurveyResponseMemberRepository surveyResponseMemberRepository;
    private final SurveyResponseDetailRepository surveyResponseDetailRepository;

    @Override
    @Transactional(readOnly = true)
    public SurveyResponseResultDto searchBySurveyResponseResult(final Long surveyId, final Pageable pageable) {
        final Survey bySurvey = this.findBySurvey(surveyId);
        final SurveyDto surveyInfo = SurveyDto.from(bySurvey);
        final List<Long> questions = this.questionsIdsBySurveyId(surveyId);
        final Page<SurveyResponseMemberDto> resultList = this.searchAllSurveyResponseBySurveyId(surveyId, pageable);

        return new SurveyResponseResultDto(surveyInfo, questions, resultList);
    }

    @Override
    @Transactional
    public Long saveSurveyResponses(final SurveyResponseDto requestDto) {
        final Survey survey = this.findBySurvey(requestDto.getSurveyId());
        final SurveyResponseMember responseMember = this.createSurveyResponseMember(survey, requestDto);

        this.createSurveyResponseDetail(responseMember, requestDto.getAnswers());
        responseMember.validate();

        return surveyResponseMemberRepository.save(responseMember).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SurveyResponseDetailDto> findSurveyResponseDetailByMemberId(final Long responseMemberId) {
        return surveyResponseMemberRepository
                .findSurveyResponseDetailByMemberIds(Collections.singletonList(responseMemberId));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, Map<Long, SurveyAnswerStatisticsDto>> getSurveyStatistics(final Long surveyId) {
        return surveyResponseDetailRepository.getSurveyStatistics(surveyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SurveyOverallCommentDto> findOverallCommentsBySurveyId(final Long surveyId) {
        return surveyResponseDetailRepository.findOverallCommentsBySurveyId(surveyId);
    }

    @Override
    @Transactional(readOnly = true)
    public Workbook searchSurveyResultExcelList(final Long surveyId) {
        final Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        final SurveyResponseResultDto result = this.searchBySurveyResponseResult(surveyId, pageable);
        final Page<SurveyResponseMemberDto> page = result.getResultList();
        final List<SurveyResponseMemberDto> list = page.getContent();

        //워크북 생성
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(String.format("%s 목록", result.getSurveyInfo().getTitle()));

        //스타일(서식)
        CellStyle headStyle = workbook.createCellStyle();
        CellStyle bodyStyle = workbook.createCellStyle();

        // 가는 경계선을 가집니다.
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);

        // 배경 그레이 색
        headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 데이터는 가운데 정렬합니다.
        headStyle.setAlignment(HorizontalAlignment.CENTER);

        // 데이터용 경계 스타일 테두리만 지정
        bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);
        bodyStyle.setAlignment(HorizontalAlignment.CENTER);

        int rowIndex = 0;
        int cellIndex = 0;

        Row headerRow = sheet.createRow(rowIndex++);
        Cell cell;

        // 헤더 생성
        String[] headerColumns = Stream.of(
                Stream.of("No.", "이름", "휴대폰 번호", "이메일"),
                Stream.iterate(1, i -> i + 1).limit(result.getQuestions().size()).map(i -> "문항" + i),
                Stream.of("기타", "등록일")
        ).flatMap(s -> s).toArray(String[]::new);

        for (int j = 0; j < headerColumns.length; j++) {
            cell = headerRow.createCell(j);
            cell.setCellStyle(headStyle);
            cell.setCellValue(headerColumns[j]);
            sheet.setColumnWidth(j, 6000);
        }

        for (SurveyResponseMemberDto vo : list) {
            XSSFRow row = sheet.createRow(rowIndex++);
            cellIndex = 0;

            // No.
            cell = row.createCell(cellIndex++);
            cell.setCellValue(page.getTotalElements() - (long) page.getNumber() * page.getSize() - rowIndex + 2);
            cell.setCellStyle(bodyStyle);

            cell = row.createCell(cellIndex++);
            cell.setCellValue(vo.getRespondentName());
            cell.setCellStyle(bodyStyle);

            cell = row.createCell(cellIndex++);
            cell.setCellValue(vo.getRespondentPhone());
            cell.setCellStyle(bodyStyle);

            cell = row.createCell(cellIndex++);
            cell.setCellValue(vo.getRespondentEmail());
            cell.setCellStyle(bodyStyle);

            for (Long questionId : result.getQuestions()) {
                cell = row.createCell(cellIndex++);
                cell.setCellValue(vo.getQuestions().get(vo.getId()).get(questionId));
                cell.setCellStyle(bodyStyle);
            }

            cell = row.createCell(cellIndex++);
            cell.setCellValue(!hasText(vo.getOverallComment()) ? "-" : "O");
            cell.setCellStyle(bodyStyle);

            cell = row.createCell(cellIndex++);
            cell.setCellValue(vo.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            cell.setCellStyle(bodyStyle);

        }

        return workbook;
    }

    /**
     * 해당 설문의 응답한 회원의 정보를 조회
     */
    private Page<SurveyResponseMemberDto> searchAllSurveyResponseBySurveyId(final Long surveyId,
                                                                            final Pageable pageable
    ) {
        return surveyResponseMemberRepository.searchAllSurveyResponseBySurveyId(surveyId, pageable);
    }

    /**
     * 회원이 응답한 내역을 해당 문항의 맞는 답변을 추가
     */
    private void createSurveyResponseDetail(final SurveyResponseMember responseMember,
                                            final List<SurveyResponseDto.SurveyAnswerDto> answers
    ) {
        answers.forEach(answerDto -> this.responseMemberAddDetail(responseMember, answerDto));
    }

    /**
     * 응답자 정보를 저장하기위해 SurveyResponseMember 생성
     */
    private SurveyResponseMember createSurveyResponseMember(final Survey survey, final SurveyResponseDto requestDto) {
        return new SurveyResponseMember(survey,
                requestDto.getRespondentInfo().getName(), // 이름
                requestDto.getRespondentInfo().getPhone(),// 폰
                requestDto.getRespondentInfo().getEmail(),// 이메일
                RequestUtils.getRemoteAddr(),             // IP
                requestDto.getOverallComment());          // 기타 자유입력
    }

    /**
     * 응답자가 응답을한 내역을 추가
     */
    private void responseMemberAddDetail(final SurveyResponseMember responseMember,
                                         final SurveyResponseDto.SurveyAnswerDto answerDto
    ) {
        final SurveyQuestion question = this.findQuestionById(answerDto.getQuestionId());
        final SurveyQuestionAnswer answer = this.findAnswerById(answerDto.getAnswerId()).orElse(null);

        responseMember.addResponse(question, answer, answerDto.getCustomAnswer());
    }

    private Survey findBySurvey(final Long id) {
        return surveyRepository.findById(id)
                .orElseThrow(() -> new ApiNotFoundException("해당 설문을 찾을 수 없습니다."));
    }

    private SurveyQuestion findQuestionById(final Long questionId) {
        return surveyQuestionRepository.findById(questionId)
                .orElseThrow(() -> new ApiNotFoundException("해당 문항을 찾을 수 없습니다."));
    }

    /**
     * 해당 설문의 대한 질문의 시퀀스를 목록으로 반환
     */
    private List<Long> questionsIdsBySurveyId(final Long surveyId) {
        return surveyQuestionRepository.findQuestionIdsBySurveyId(surveyId);
    }

    /**
     * 설문의 답변 시퀀스로 답변 조회
     */
    private Optional<SurveyQuestionAnswer> findAnswerById(final Long answerId) {
        return Optional.ofNullable(answerId)
                .flatMap(id -> questionAnswerRepository.findById(id)
                        .map(Optional::of)
                        .orElseThrow(() -> new ApiNotFoundException("해당 답변을 찾을 수 없습니다.")));
    }

}
