package tcc.uff.resource.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
//    @Bean
//    JwtAuthenticationConverter jwtAuthenticationConverter() {
//        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
//        grantedAuthoritiesConverter.setAuthorityPrefix("");
//
//        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
//        return jwtAuthenticationConverter;
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers("/", "/attendences/ws/**", "/login", "/login-error", "/app.js", "/main.css", "/loginstyle.css", "/qrcode.min.js", "/models/**").permitAll()
                        .anyRequest().authenticated()
                )
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .oauth2ResourceServer(c -> c.jwt(Customizer.withDefaults()))
                .build();
    }
}