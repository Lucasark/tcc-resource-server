package tcc.uff.resource.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import tcc.uff.resource.server.converter.mongo.AttendenceEnumReadConverter;
import tcc.uff.resource.server.converter.mongo.AttendenceEnumWriteConverter;

import java.util.ArrayList;
import java.util.List;

//class MongoDBConfig extends AbstractMongoClientConfiguration {

@Configuration
class MongoDBConfig {

//    @Value("${DB_NAME}")
//    private String dbName;
//
//    @Value("${MONGO_URI}")
//    private String uri;
//
//    @Override
//    public @Bean
//    @NonNull MongoClient mongoClient() {
//        return MongoClients.create(uri);
//    }
//
//    @Bean
//    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
//        return new MongoTransactionManager(dbFactory);
//    }
//
//    @Override
//    protected @NonNull String getDatabaseName() {
//        return dbName;
//    }


    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converterList = new ArrayList<>();
        converterList.add(new AttendenceEnumReadConverter());
        converterList.add(new AttendenceEnumWriteConverter());
        return new MongoCustomConversions(converterList);
    }

}