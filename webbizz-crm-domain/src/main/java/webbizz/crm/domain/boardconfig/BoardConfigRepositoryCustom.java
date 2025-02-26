package webbizz.crm.domain.boardconfig;

import java.util.List;
import java.util.Map;

public interface BoardConfigRepositoryCustom {

    /**
     * 게시판 설정 정보 조회 [단일]
     *
     * @param boardId 게시판 시퀀스
     * @return 게시판 설정 정보 (키, 값)
     * @author TAEROK HWANG
     */
    Map<String, String> searchByBoardId(Long boardId);

    /**
     * 게시판 설정 정보 조회 [다중]
     *
     * @param boardIds 게시판 시퀀스 리스트
     * @return 게시판 설정 정보 (게시판 시퀀스, (키, 값))
     * @author TAEROK HWANG
     */
    Map<Long, Map<String, String>> searchAllByBoardIds(List<Long> boardIds);

    /**
     * 게시판 설정 정보 삭제 (hard delete)
     *
     * @param boardId 게시판 시퀀스
     * @author TAEROK HWANG
     */
    void deleteByBoardId(Long boardId);

}
