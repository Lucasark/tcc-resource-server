package tcc.uff.resource.server.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.SupplierJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class WebSocketAuthCookie extends OncePerRequestFilter {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    public WebSocketAuthCookie(SupplierJwtDecoder decoder) {
        this.jwtAuthenticationProvider = new JwtAuthenticationProvider(decoder);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {

        for (Cookie cookie : request.getCookies()) {
            if ("token".equals(cookie.getName())) {
                var auth = jwtAuthenticationProvider.authenticate(new BearerTokenAuthenticationToken(cookie.getValue()));
                SecurityContextHolder.getContext().setAuthentication(auth);
                filterChain.doFilter(request, response);
                return;
            }
        }

        throw new RuntimeException("No JWT token present in cookies");
    }
}
