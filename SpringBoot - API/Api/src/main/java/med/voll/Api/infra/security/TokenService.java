package med.voll.Api.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
// import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import med.voll.Api.usuario.Usuario;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret; 

    public String gerarToken(Object usuario){
        try {
            var algoritimo = Algorithm.HMAC256(secret);
            return JWT.create().withIssuer("Api Voll.med").withSubject(((Usuario) usuario).getLogin()).withExpiresAt(dataExpiracao()).sign(algoritimo);
        } catch (JWTCreationException exception){
            // Invalid Signing configuration / Couldn't convert Claims.
            throw new RuntimeException("Erro ao gerar token JWT", exception); 
        }
    }

    // create a code to valid the token
    public String getSubject(String tokenJWT){
        try {
            var algoritimo = Algorithm.HMAC256(secret);
            return JWT.require(algoritimo).withIssuer("Api Voll.med").build().verify(tokenJWT).getSubject();
            } catch (JWTVerificationException exception) {
                throw new RuntimeException("Token JWT invalido ou expirado"); 
            }
    }
    

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}
