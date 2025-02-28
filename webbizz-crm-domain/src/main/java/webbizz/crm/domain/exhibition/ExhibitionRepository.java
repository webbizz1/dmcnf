package webbizz.crm.domain.exhibition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import webbizz.crm.domain.exhibition.entity.Exhibition;
import webbizz.crm.domain.exhibition.enumset.ExhibitionType;

import java.util.Optional;

public interface ExhibitionRepository extends
        JpaRepository<Exhibition, Long>,
        ExhibitionRepositoryCustom {

    /**
     * 전시 시퀀스와 전시 유형으로 전시 조회
     *
     * @param id 전시 시퀀스
     * @param type 전시 유형
     * @return 전시 Entity (Optional)
     * @author TAEROK HWANG
     */
    @Query("SELECT e FROM Exhibition e WHERE e.id = :id AND e.type = :exhibitionType AND e.delYn = 'N'")
    Optional<Exhibition> findBy(@Param("id") Long id, @Param("exhibitionType") ExhibitionType type);

}
