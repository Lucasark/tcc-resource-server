package tcc.uff.resource.server.converter.mongo;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import tcc.uff.resource.server.model.enums.UserContactEnum;

@ReadingConverter
public class UserContactEnumReadConverter implements Converter<Integer, UserContactEnum> {

    @Override
    public UserContactEnum convert(@NonNull Integer source) {
        return UserContactEnum.fromId(source);
    }
}
