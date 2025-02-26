package webbizz.crm.domain.authoritypattern;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import webbizz.crm.domain.authoritypattern.entity.AuthorityPattern;
import webbizz.crm.domain.member.enumset.MemberRole;

import java.util.List;

public interface AuthorityPatternRepository extends
        JpaRepository<AuthorityPattern, Long>,
        AuthorityPatternRepositoryCustom {

    /**
     * 권한에 따른 모든 권한 패턴 조회 (Entity)
     * @param role 권한
     * @return 권한 패턴 Entity 리스트
     * @author TAEROK HWANG
     */
    List<AuthorityPattern> findAllByRole(@Param("role") MemberRole role);

}
