package webbizz.crm.domain.attachment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import webbizz.crm.domain.attachment.entity.Attachment;

import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    @Query("SELECT a FROM Attachment a WHERE a.uuid = :uuid AND a.delYn = 'N'")
    Optional<Attachment> findByUUID(@Param("uuid") String uuid);

}
