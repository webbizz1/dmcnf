package webbizz.crm.domain.boardconfig;

import org.springframework.data.jpa.repository.JpaRepository;
import webbizz.crm.domain.boardconfig.entity.BoardConfig;

public interface BoardConfigRepository extends
        JpaRepository<BoardConfig, Long>,
        BoardConfigRepositoryCustom {
}
