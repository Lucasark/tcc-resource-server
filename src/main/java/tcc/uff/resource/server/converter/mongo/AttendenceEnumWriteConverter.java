package tcc.uff.resource.server.converter.mongo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import tcc.uff.resource.server.model.enums.AttendenceEnum;

@WritingConverter
public class AttendenceEnumWriteConverter implements Converter<AttendenceEnum, Integer> {

    @Override
    public Integer convert(AttendenceEnum source) {
        return source.getId();
    }
}
