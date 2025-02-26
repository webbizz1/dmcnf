package webbizz.crm.domain.visitorlog;

import org.springframework.data.jpa.repository.JpaRepository;
import webbizz.crm.domain.visitorlog.entity.VisitorLog;

public interface VisitorLogRepository extends
        JpaRepository<VisitorLog, Long>,
        VisitorLogRepositoryCustom {
}
