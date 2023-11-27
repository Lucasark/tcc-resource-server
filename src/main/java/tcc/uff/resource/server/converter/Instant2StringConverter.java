package tcc.uff.resource.server.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import tcc.uff.resource.server.utils.Constants;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Instant2StringConverter extends StdConverter<Instant, String> {
    @Override
    public String convert(Instant instant) {
        return instant.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(Constants.OFF_DATE_TIME_PATTERN));
    }
}
