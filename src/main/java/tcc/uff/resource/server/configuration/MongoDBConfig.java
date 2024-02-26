package tcc.uff.resource.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import tcc.uff.resource.server.converter.mongo.AttendanceEnumReadConverter;
import tcc.uff.resource.server.converter.mongo.AttendanceEnumWriteConverter;
import tcc.uff.resource.server.converter.mongo.UserContactEnumReadConverter;
import tcc.uff.resource.server.converter.mongo.UserContactEnumWriteConverter;

import java.util.ArrayList;
import java.util.List;

@Configuration
class MongoDBConfig {

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converterList = new ArrayList<>();
        converterList.add(new AttendanceEnumReadConverter());
        converterList.add(new AttendanceEnumWriteConverter());
        converterList.add(new UserContactEnumReadConverter());
        converterList.add(new UserContactEnumWriteConverter());
        return new MongoCustomConversions(converterList);
    }

}