package webbizz.crm.domain.memberauditor;

import org.springframework.data.jpa.repository.JpaRepository;
import webbizz.crm.domain.memberauditor.entity.MemberAuditor;

public interface MemberAuditorRepository extends JpaRepository<MemberAuditor, Long> {
}
