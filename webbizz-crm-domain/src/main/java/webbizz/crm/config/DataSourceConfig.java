package webbizz.crm.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.Map;

/**
 * <p>Primary DataSource 설정</p>
 *
 * eGovFrame 호환성 검증 시 게시판 샘플 코드로 연동 테스트를 하게 되는데 기본적으로 HyperSQL DB (InMemory Database) 를 사용함 <br />
 * 이 경우, <code>application.properties</code> 에서 설정한 값이 무시됨 <br /><br />
 *
 * 호환성 검증이 필요없는 경우 현재 파일 삭제 및 QueryDslConfig 파일에서 {@link PersistenceContext} 의 unitName 삭제 <br /><br />
 *
 * 다중 DataSource 를 사용할 경우: <br />
 * - 이 클래스를 복사하여 추가하고 <code>static final</code> 상수 변경 및 {@link Primary @Primary} 제거 <br />
 * - {@link EnableJpaRepositories} 에서
 * {@link EnableJpaRepositories#basePackages basePackages} 변경하고
 * {@link EnableJpaRepositories#excludeFilters excludeFilters} 추가 <br />
 * - 추가한 클래스의 {@link HikariConfig} Bean 제거 (중복 선언 Bean) <br />
 * - Configuration 파일에 {@link EntityManager}, JPAQueryFactory Bean 추가
 *
 * @author TAEROK HWANG
 * @since 2024-09-05
 */
@Configuration
@EnableJpaRepositories(
        basePackages = DataSourceConfig.BASE_PACKAGE,
        entityManagerFactoryRef = DataSourceConfig.ENTITY_MANAGER_FACTORY,
        transactionManagerRef = DataSourceConfig.TRANSACTION_MANAGER)
@EnableTransactionManagement
public class DataSourceConfig {

    public static final String BASE_PACKAGE = "webbizz.crm.domain";
    public static final String BEAN_NAME_PREFIX = "primary";
    public static final String PROPERTIES_PREFIX = "spring.datasource";
    public static final String PROPERTIES_HIKARI_PREFIX = "spring.datasource.hikari";

    public static final String DATA_SOURCE = BEAN_NAME_PREFIX + "DataSource";
    public static final String DATA_SOURCE_PROPERTIES = BEAN_NAME_PREFIX + "DataSourceProperties";
    public static final String ENTITY_MANAGER = BEAN_NAME_PREFIX + "EntityManager";
    public static final String ENTITY_MANAGER_FACTORY = BEAN_NAME_PREFIX + "EntityManagerFactory";
    public static final String HIKARI_PROPERTIES = BEAN_NAME_PREFIX + "HikariConfig";
    public static final String TRANSACTION_MANAGER = BEAN_NAME_PREFIX + "TransactionManager";

    @Bean(DATA_SOURCE_PROPERTIES)
    @Primary
    @ConfigurationProperties(prefix = PROPERTIES_PREFIX)
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(HIKARI_PROPERTIES)
    @Primary
    @ConfigurationProperties(PROPERTIES_HIKARI_PREFIX)
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    @Bean(DATA_SOURCE)
    @Primary
    public DataSource dataSource(@Qualifier(DATA_SOURCE_PROPERTIES) DataSourceProperties dataSourceProperties,
                                 @Qualifier(HIKARI_PROPERTIES) HikariConfig hikariConfig) {

        HikariDataSource hikariDataSource = dataSourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();

        hikariDataSource.setMaximumPoolSize(hikariConfig.getMaximumPoolSize());

        return hikariDataSource;
    }

    @Bean(ENTITY_MANAGER_FACTORY)
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier(DATA_SOURCE) DataSource dataSource,
                                                                       JpaProperties jpaProperties,
                                                                       HibernateProperties hibernateProperties,
                                                                       EntityManagerFactoryBuilder builder) {

        // application.properties 에서 설정한 JPA, Hibernate 설정 값을 사용하기
        Map<String, Object> properties = hibernateProperties.determineHibernateProperties(
                jpaProperties.getProperties(), new HibernateSettings());

        return builder
                .dataSource(dataSource)
                .packages(BASE_PACKAGE)
                .persistenceUnit(ENTITY_MANAGER)
                .properties(properties)
                .build();
    }

    @Bean(TRANSACTION_MANAGER)
    @Primary
    public PlatformTransactionManager transactionManager(@Qualifier(ENTITY_MANAGER_FACTORY)
                                                         EntityManagerFactory entityManagerFactory) {

        return new JpaTransactionManager(entityManagerFactory);
    }

}
