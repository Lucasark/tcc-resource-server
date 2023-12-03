package tcc.uff.resource.server.configuration;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriTemplate;
import tcc.uff.resource.server.model.handler.AttendenceHandler;
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

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new CustomWebSocketHandler(attendences), "/attendences/ws/courses/{c}/dates/{d}")
                .setAllowedOriginPatterns("*")
                .setAllowedOrigins("*")
                .addInterceptors(auctionInterceptor());
    }

    @Bean
    public HandshakeInterceptor auctionInterceptor() {

        return new HandshakeInterceptor() {
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                           WebSocketHandler wsHandler, Map<String, Object> attributes) {

                String path = request.getURI().getPath();
                UriTemplate uriTemplate = new UriTemplate("/v1/api/attendences/ws/courses/{courseId}/dates/{date}");
                var courseId = uriTemplate.match(path).get("courseId");
                var date = Instant.parse(uriTemplate.match(path).get("date"));

//                frequencyService.initFrenquency(courseId, date);

                return true;
            }

            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                       WebSocketHandler wsHandler, Exception exception) {

                log.info("aqui");
            }
        };
    }
}