package tcc.uff.resource.server.converter.mongo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import tcc.uff.resource.server.model.enums.UserContactEnum;

@WritingConverter
public class UserContactEnumWriteConverter implements Converter<UserContactEnum, Integer> {

    @Override
    public Integer convert(UserContactEnum source) {
        return source.getId();
    }
}
