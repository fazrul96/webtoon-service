package com.webtoon_service.config.datasource;

import com.webtoon_service.exception.BaseException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import static com.webtoon_service.exception.BatchErrorType.GENERIC_ERROR;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.webtoon_service.domain.data",
        entityManagerFactoryRef = "webtoonEntityManagerFactory",
        transactionManagerRef = "webtoonTransactionManager"
)
@SuppressWarnings({"PMD.AvoidUncheckedExceptionsInSignatures"})
public class WebtoonDataSourcesConfiguration {
    @Bean("webtoonConfig")
    @ConfigurationProperties("spring.datasource.webtoon")
    public HikariConfig appHikariConfig() {
        return new HikariConfig();
    }

    @Bean("webtoonDataSource")
    public HikariDataSource appDataSource(@Qualifier("webtoonConfig") HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }

    @Bean(name = "webtoonEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
        @Autowired EntityManagerFactoryBuilder builder,
        @Qualifier("webtoonDataSource") DataSource webtoonDataSource) {
        return builder
                .dataSource(webtoonDataSource)
                .packages("com.webtoon_service.domain.data")
                .build();
    }

    @Bean("webtoonTransactionManager")
    public PlatformTransactionManager platformTransactionManager(
            @Qualifier("webtoonEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean)
            throws BaseException {
        if (entityManagerFactoryBean == null) {
            throw new BaseException(GENERIC_ERROR);
        } else {
            EntityManagerFactory emf = entityManagerFactoryBean.getObject();
            if (emf == null) {
                throw new BaseException(GENERIC_ERROR);
            } else {
                return new JpaTransactionManager(emf);
            }
        }
    }
}