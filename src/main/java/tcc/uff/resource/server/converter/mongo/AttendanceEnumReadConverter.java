package tcc.uff.resource.server.converter.mongo;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import tcc.uff.resource.server.model.enums.AttendanceEnum;

@ReadingConverter
public class AttendanceEnumReadConverter implements Converter<Integer, AttendanceEnum> {

    @Override
    public AttendanceEnum convert(@NonNull Integer source) {
        return AttendanceEnum.fromId(source);
    }
}
