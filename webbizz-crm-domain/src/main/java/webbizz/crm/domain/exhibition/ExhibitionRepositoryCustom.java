package webbizz.crm.domain.exhibition;

import org.springframework.data.domain.Page;
import webbizz.crm.domain.exhibition.dto.ExhibitionCondition;
import webbizz.crm.domain.exhibition.dto.ExhibitionDto;
import webbizz.crm.domain.exhibition.enumset.ExhibitionType;

import java.util.List;
import java.util.Optional;

public interface ExhibitionRepositoryCustom {

    /**
     * 전시 시퀀스와 전시 유형으로 전시 정보 조회
     *
     * @param id 전시 시퀀스
     * @param type 전시 유형
     * @return 전시 DTO (Optional)
     * @author TAEROK HWANG
     */
    Optional<ExhibitionDto> searchBy(Long id, ExhibitionType type);

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
     * @return 전시 수 (Long)
     * @author TAEROK HWANG
     */
    Long countExhibitionForActive(ExhibitionType type);

}
