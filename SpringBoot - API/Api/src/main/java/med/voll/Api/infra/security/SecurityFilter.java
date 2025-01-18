package med.voll.Api.infra.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.var;
import med.voll.Api.usuario.UsuarioRepository;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Add your security logic here
        // @SuppressWarnings("unused")
        var tokenJWT = recuperarToken(request);
        if (tokenJWT != null) {
            // @SuppressWarnings("unused")
            var subject = tokenService.getSubject(tokenJWT); 
            var usuario = repository.findByLogin(subject);
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
        
    private String recuperarToken(HttpServletRequest request) {
        // Add your logic to retrieve the token from the request
        var authorizationHeader = request.getHeader("Authorizatiohn");
        if (authorizationHeader != null ) {
            return authorizationHeader.replace("Bearer", " ");
        }
        return null;
    }

}
