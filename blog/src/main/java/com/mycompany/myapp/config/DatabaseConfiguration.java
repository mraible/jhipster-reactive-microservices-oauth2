package com.mycompany.myapp.config;

import io.github.jhipster.config.JHipsterConstants;
import com.github.mongobee.Mongobee;
import com.mongodb.MongoClient;
import io.github.jhipster.domain.util.JSR310DateConverters.DateToZonedDateTimeConverter;
import io.github.jhipster.domain.util.JSR310DateConverters.ZonedDateTimeToDateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableReactiveMongoRepositories("com.mycompany.myapp.repository")
@Profile("!" + JHipsterConstants.SPRING_PROFILE_CLOUD)
@Import(value = {MongoAutoConfiguration.class, MongoReactiveAutoConfiguration.class})
public class DatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener() {
        return new ValidatingMongoEventListener(validator());
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(DateToZonedDateTimeConverter.INSTANCE);
        converters.add(ZonedDateTimeToDateConverter.INSTANCE);
        return new MongoCustomConversions(converters);
    }

    @Bean
    public Mongobee mongobee(MongoClient mongoClient, MongoTemplate mongoTemplate, MongoProperties mongoProperties) {
        log.debug("Configuring Mongobee");
        Mongobee mongobee = new Mongobee(mongoClient);
        mongobee.setDbName(mongoProperties.getMongoClientDatabase());
        mongobee.setMongoTemplate(mongoTemplate);
        // package to scan for migrations
        mongobee.setChangeLogsScanPackage("com.mycompany.myapp.config.dbmigrations");
        mongobee.setEnabled(true);
        return mongobee;
    }}
