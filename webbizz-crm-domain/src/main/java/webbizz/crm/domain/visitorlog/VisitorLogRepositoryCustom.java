package webbizz.crm.domain.visitorlog;

import webbizz.crm.domain.visitorlog.dto.VisitorLogSaveDto;

public interface VisitorLogRepositoryCustom {

    /**
     * 방문자 로그 저장
     *
     * @param requestDto 방문자 로그 저장 DTO
     * @author TAEROK HWANG
     */
    void insertOrUpdate(VisitorLogSaveDto requestDto);

    /**
     * 누적 방문자 수 조회
     */
    Long countTotalVisitor();

}
