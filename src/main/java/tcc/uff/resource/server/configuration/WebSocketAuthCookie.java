package tcc.uff.resource.server.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.SupplierJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.web.filter.OncePerRequestFilter;
import tcc.uff.resource.server.model.response.ErrorResponse;
import tcc.uff.resource.server.utils.MethodsUtils;

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

        var a = request.getHeader("sec-websocket-protocol");

        log.info("ENTREI AQUI! " + a);

        if (a != null) {
            var auth = jwtAuthenticationProvider.authenticate(new BearerTokenAuthenticationToken(a));
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
            log.info("CABOU!" + a);
            return;
        }

        responseUnauthorized(response);
    }

    private void responseUnauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(MethodsUtils.convertObjectToJson(
                ErrorResponse.builder()
                        .message("Não autorizado!")
                        .description("Obrigatorio incluir nos Cookies o Token com contexto 'token' !")
                        .build()
        ));
    }
}
