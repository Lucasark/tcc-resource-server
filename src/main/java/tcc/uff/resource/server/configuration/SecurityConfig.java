package tcc.uff.resource.server.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.oauth2.jwt.SupplierJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://alunopresente-hml.vercel.app", "https://alunopresente.vercel.app","http://localhost:3000/"));
        configuration.setAllowedMethods(Arrays.asList("POST", "PATCH", "GET", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Headers", "Content-Type", "Authorization", "Content-Length", "X-Requested-With", "Accept"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers("/", "/login", "/login-error", "/*.css", "/*.js", "/*.swf", "/tests/**", "/error/**", "/models/**", "/batch-process/**",
                                "/swagger-ui/**", "/v3/**","/attendances/ws/**", "/*.ico").permitAll()
                        .anyRequest().authenticated()
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .oauth2ResourceServer(c -> c.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.debug(false);
    }

    @Bean
    public FilterRegistrationBean<WebSocketAuthCookie> webSocketCookieBearerTokenAuthorisationFilterRegistration(SupplierJwtDecoder decoder){
        FilterRegistrationBean<WebSocketAuthCookie> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new WebSocketAuthCookie(decoder));
        registrationBean.addUrlPatterns("/attendances/ws/*");
        registrationBean.setOrder(2);

        return registrationBean;
    }
}