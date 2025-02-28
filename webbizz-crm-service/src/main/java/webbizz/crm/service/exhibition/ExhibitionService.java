package webbizz.crm.service.exhibition;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import webbizz.crm.domain.exhibition.dto.ExhibitionCondition;
import webbizz.crm.domain.exhibition.dto.ExhibitionDto;
import webbizz.crm.domain.exhibition.dto.ExhibitionSaveDto;
import webbizz.crm.domain.exhibition.enumset.ExhibitionType;

import java.util.List;

public interface ExhibitionService {

    /**
     * 전시 시퀀스와 전시 유형으로 전시 정보 조회
     *
     * @param id 전시 시퀀스
     * @param type 전시 유형
     * @return 전시 DTO
     * @author TAEROK HWANG
     */
    ExhibitionDto searchBy(Long id, ExhibitionType type);

    /**
     * 검색 조건으로 전시 목록 조회
     *
     * @param condition 검색 조건
     * @return 전시 DTO 리스트 (Page)
     * @author TAEROK HWANG
     */
    Page<ExhibitionDto> searchAllByCondition(ExhibitionCondition condition);

    /**
     * 활성화된 전시 목록 조회
     *
     * @return 전시 DTO 리스트
     * @author TAEROK HWANG
     */
    List<ExhibitionDto> searchAllForActive();

    /**
     * 활성화된 전시 수 조회
     *
     * @param type 전시 유형 (NULL 일 경우 전체 조회)
     * @return 전시 수 (Long)
     * @author TAEROK HWANG
     */
    Long countExhibitionForActive(ExhibitionType type);

    /**
     * 전시 정보 등록·수정
     *
     * @param requestDto 전시 정보 저장 요청 DTO
     * @param httpMethod HTTP 메소드
     * @return 전시 시퀀스
     * @author TAEROK HWANG
     */
    Long saveExhibition(final ExhibitionSaveDto requestDto, final HttpMethod httpMethod);

    /**
     * 전시 정보 삭제
     *
     * @param id 전시 시퀀스
     * @author TAEROK HWANG
     */
    void deleteExhibition(Long id);

}
