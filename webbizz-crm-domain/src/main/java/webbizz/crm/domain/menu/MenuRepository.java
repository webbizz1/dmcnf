package webbizz.crm.domain.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import webbizz.crm.domain.menu.entity.Menu;

import java.util.List;

public interface MenuRepository extends
        JpaRepository<Menu, Long>,
        MenuRepositoryCustom {

    /**
     * groupId 에 해당하고 삭제되지 않은 모든 메뉴 목록을 조회
     *
     * @param groupId 그룹 ID
     * @return 메뉴 리스트 (Entity)
     * @author TAEROK HWANG
     */
    @Query("SELECT m FROM Menu m where m.groupId = :groupId AND m.delYn = 'N'")
    List<Menu> findAllByGroupId(@Param("groupId") Long groupId);

}
