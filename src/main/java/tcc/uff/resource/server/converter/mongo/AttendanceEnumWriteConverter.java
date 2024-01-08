package tcc.uff.resource.server.converter.mongo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import tcc.uff.resource.server.model.enums.AttendanceEnum;

@WritingConverter
public class AttendanceEnumWriteConverter implements Converter<AttendanceEnum, Integer> {

    @Override
    public Integer convert(AttendanceEnum source) {
        return source.getId();
    }
}
