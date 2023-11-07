package tcc.uff.resource.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tcc.uff.resource.server.model.document.AttendenceHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class LocalAttedences {

    private final Map<String, AttendenceHandler> attendences = new HashMap<>();

    @Bean("attendences")
    public Map<String, AttendenceHandler> getAttendences() {
        return attendences;
    }

}
