package webbizz.crm.domain.terms;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import webbizz.crm.domain.terms.entity.Terms;
import webbizz.crm.domain.terms.enumset.TermsType;

import java.util.List;

public interface TermsRepository extends
        JpaRepository<Terms, Long>,
        TermsRepositoryCustom {

    /**
     * 약관 유형으로 약관 목록 조회
     *
     * @param type 약관 유형
     * @return 약관 리스트 (Entity)
     * @author TAEROK HWANG
     */
    @Query("SELECT t FROM Terms t WHERE t.type = :type AND t.delYn = 'N'")
    List<Terms> findAllByType(@Param("type") TermsType type);

}
