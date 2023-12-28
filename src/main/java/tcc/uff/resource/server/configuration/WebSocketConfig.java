package tcc.uff.resource.server.configuration;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import tcc.uff.resource.server.handler.AttendenceWebSocketHandler;
import tcc.uff.resource.server.model.handler.AttendenceHandler;
import tcc.uff.resource.server.service.CourseService;
import tcc.uff.resource.server.service.impl.FrequencyServiceImpl;

import java.util.Map;

@Slf4j
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final Map<String, AttendenceHandler> attendences;

    private final FrequencyServiceImpl frequencyService;

    private final TaskScheduler taskScheduler;

    private final CourseService courseService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new AttendenceWebSocketHandler(taskScheduler, attendences, courseService, frequencyService), "/attendances/ws")
                .setAllowedOriginPatterns("*");
    }
}