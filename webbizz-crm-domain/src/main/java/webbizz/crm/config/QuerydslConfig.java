package webbizz.crm.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class QuerydslConfig {

    /**
     * EntityManager Bean <br />
     * 같은 패키지 내에 DataSourceConfig 파일이 없다면 unitName 을 삭제할 것
     */
    @PersistenceContext(unitName = DataSourceConfig.ENTITY_MANAGER)
    private EntityManager entityManager;

    /**
     * QueryDSL RequireAllArgsConstructor 사용을 위한 설정
     *
     * @return JPAQueryFactory
     */
    @Bean
    public JPAQueryFactory queryFactory() {
        return new JPAQueryFactory(entityManager);
    }

}
