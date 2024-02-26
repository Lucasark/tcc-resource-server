package tcc.uff.resource.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tcc.uff.resource.server.model.handler.AttendanceHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class LocalAttedencesConfig {

    private final Map<String, AttendanceHandler> attendances = new HashMap<>();

    @Bean("attendances")
    public Map<String, AttendanceHandler> getAttendances() {
        return attendances;
    }


}
