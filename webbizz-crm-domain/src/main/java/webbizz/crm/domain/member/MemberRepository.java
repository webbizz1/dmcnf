package webbizz.crm.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import webbizz.crm.domain.member.entity.Member;
import webbizz.crm.domain.member.enumset.MemberRole;

import java.util.Optional;

public interface MemberRepository extends
        JpaRepository<Member, Long>,
        MemberRepositoryCustom {

    /**
     * 회원 시퀀스와 권한으로 회원 조회
     *
     * @param id 회원 시퀀스
     * @param role 회원 권한
     * @return 회원 Entity (Optional)
     * @author TAEROK HWANG
     */
    @Query("SELECT m FROM Member m WHERE m.id = :id AND m.role = :role AND m.delYn = 'N'")
    Optional<Member> findBy(@Param("id") Long id, @Param("role") MemberRole role);

    /**
     * 로그인 아이디로 회원 조회
     *
     * @param loginId 로그인 아이디
     * @param role 회원 권한
     * @return 회원 Entity (Optional)
     * @author TAEROK HWANG
     */
    @Query("SELECT m FROM Member m WHERE m.loginId = :loginId AND m.role = :role AND m.delYn = 'N'")
    Optional<Member> findBy(@Param("loginId") String loginId, @Param("role") MemberRole role);

    /**
     * 로그인 아이디로 회원 수 조회 (로그인 아이디 중복 체크)
     *
     * @param loginId 로그인 아이디
     * @param role 회원 권한
     * @return 회원 수
     * @author TAEROK HWANG
     */
    @Query("SELECT COUNT(m) from Member m WHERE m.loginId = :loginId AND m.role = :role AND m.delYn = 'N'")
    Long countByLoginId(@Param("loginId") String loginId, @Param("role") MemberRole role);

}
