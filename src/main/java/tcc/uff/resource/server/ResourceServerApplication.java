package tcc.uff.resource.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import tcc.uff.resource.server.repository.impl.MongoHelperRepositoryImpl;

@EnableRetry
@EnableAsync
@EnableScheduling
@EnableMongoAuditing
@EnableMethodSecurity
@SpringBootApplication
@EnableMongoRepositories(repositoryBaseClass = MongoHelperRepositoryImpl.class)
public class ResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class, args);
    }

}
