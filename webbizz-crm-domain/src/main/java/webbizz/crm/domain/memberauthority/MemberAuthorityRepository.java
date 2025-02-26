package webbizz.crm.domain.memberauthority;

import org.springframework.data.jpa.repository.JpaRepository;
import webbizz.crm.domain.memberauthority.entity.MemberAuthority;

public interface MemberAuthorityRepository extends
        JpaRepository<MemberAuthority, Long>,
        MemberAuthorityRepositoryCustom {
}
