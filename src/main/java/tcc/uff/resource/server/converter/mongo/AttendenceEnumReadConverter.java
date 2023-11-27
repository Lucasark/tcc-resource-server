package tcc.uff.resource.server.converter.mongo;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import tcc.uff.resource.server.model.enums.AttendenceEnum;

@ReadingConverter
public class AttendenceEnumReadConverter implements Converter<Integer, AttendenceEnum> {

    @Override
    public AttendenceEnum convert(@NonNull Integer source) {
        return AttendenceEnum.fromId(source);
    }
}
