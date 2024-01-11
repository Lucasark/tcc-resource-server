package tcc.uff.resource.server.configuration;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import tcc.uff.resource.server.handler.AttendanceWebSocketHandler;
import tcc.uff.resource.server.model.handler.AttendanceHandler;
import tcc.uff.resource.server.service.AttendanceService;
import tcc.uff.resource.server.service.CourseService;
import tcc.uff.resource.server.service.impl.FrequencyServiceImpl;

import java.util.Map;

@Slf4j
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final Map<String, AttendanceHandler> attendances;

    private final FrequencyServiceImpl frequencyService;

    private final TaskScheduler taskScheduler;

    private final CourseService courseService;

    private final AttendanceService attendanceService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new AttendanceWebSocketHandler(taskScheduler, attendances, courseService, frequencyService, attendanceService), "/attendances/ws")
                .setAllowedOriginPatterns("*");
    }
}