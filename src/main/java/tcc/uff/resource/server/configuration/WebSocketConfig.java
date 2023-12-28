package tcc.uff.resource.server.configuration;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriTemplate;
import tcc.uff.resource.server.handler.AttendenceWebSocketHandler;
import tcc.uff.resource.server.model.handler.AttendenceHandler;
import tcc.uff.resource.server.service.CourseService;
import tcc.uff.resource.server.service.impl.FrequencyServiceImpl;

import java.time.Instant;
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
        registry.addHandler(new AttendenceWebSocketHandler(taskScheduler, attendences, courseService, frequencyService), "/attendences/ws/courses/{courseId}/dates/{date}")
                .setAllowedOriginPatterns("*")
                .setAllowedOrigins("*")
                .addInterceptors(auctionInterceptor());
    }

    @Bean
    public HandshakeInterceptor auctionInterceptor() {

        return new HandshakeInterceptor() {
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                           WebSocketHandler wsHandler, Map<String, Object> attributes) {
                try {
                    String path = request.getURI().getPath();
                    UriTemplate uriTemplate = new UriTemplate("/v1/api/attendences/ws/courses/{courseId}/dates/{date}");
                    var courseId = uriTemplate.match(path).get("courseId");
                    var date = Instant.parse(uriTemplate.match(path).get("date"));

                    attributes.put("courseId", courseId);
                    attributes.put("date", date);
                } catch (Exception e) {
                    response.setStatusCode(HttpStatus.I_AM_A_TEAPOT);
                    return false;
                }

                return true;
            }

            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                       WebSocketHandler wsHandler, Exception exception) {

                log.debug("AFTERHANDSHAKE");
            }
        };
    }
}