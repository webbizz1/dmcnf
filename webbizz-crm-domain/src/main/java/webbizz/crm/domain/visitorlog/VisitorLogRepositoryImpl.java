package webbizz.crm.domain.visitorlog;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import webbizz.crm.domain.visitorlog.dto.VisitorLogSaveDto;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static webbizz.crm.domain.visitorlog.entity.QVisitorLog.visitorLog;

@Repository
@RequiredArgsConstructor
public class VisitorLogRepositoryImpl implements VisitorLogRepositoryCustom {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional
    public void insertOrUpdate(VisitorLogSaveDto requestDto) {
        entityManager.createNativeQuery("INSERT INTO visitor_log " +
                        "(application_name, member_id, remote_addr, request_uri, group_date, created_at) " +
                        "VALUES (:applicationName, :memberId, :remoteAddr, :requestUri, :groupDate, :createdAt) " +
                        "ON DUPLICATE KEY UPDATE hits = hits + 1, updated_at = :updatedAt"
                )
                .setParameter("applicationName", requestDto.getApplicationName())
                .setParameter("memberId", requestDto.getMemberId())
                .setParameter("remoteAddr", requestDto.getRemoteAddr())
                .setParameter("requestUri", requestDto.getRequestUri())
                .setParameter("groupDate", LocalDate.now())
                .setParameter("createdAt", LocalDateTime.now())
                .setParameter("updatedAt", LocalDateTime.now())
                .executeUpdate();
    }

    @Override
    public Long countTotalVisitor() {
        return queryFactory
                .select(
                        visitorLog.remoteAddr
                                .concat(visitorLog.groupDate.stringValue())
                                .countDistinct())
                .from(visitorLog)
                .fetchFirst();
    }

}
