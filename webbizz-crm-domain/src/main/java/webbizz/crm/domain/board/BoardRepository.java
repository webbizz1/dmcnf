package webbizz.crm.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import webbizz.crm.domain.board.entity.Board;

public interface BoardRepository extends
        JpaRepository<Board, Long>,
        BoardRepositoryCustom {

    /**
     * 게시판 이름으로 게시판 수 조회 (게시판 이름 중복 체크)
     *
     * @param name 게시판 이름
     * @return 게시판 수
     * @author TAEROK HWANG
     */
    @Query("SELECT COUNT(b) FROM Board b WHERE b.name = :name AND b.delYn = 'N'")
    Long countByName(@Param("name") String name);

}
