package br.com.dazo.poc.security.service;

import br.com.dazo.poc.security.properties.JWTConfigProperties;
import br.com.dazo.poc.utils.DateUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class JWTService {

    public static final String BEARER_ = "Bearer ";

    @Autowired
    private JWTConfigProperties jwtConfigProperties;

    @Autowired
    private FakeStaticApplicactionUserService userService;

    public String montaRefreshToken(String url, String username) {

        Algorithm algorithm = Algorithm.HMAC256(jwtConfigProperties.getSecret().getBytes());
        LocalDateTime nowPlusTimeTimeRefresh = LocalDateTime.now().plusMinutes(jwtConfigProperties.getTimeRefresh());

        String refreshToken = JWT.create()
                .withSubject(username)
                .withExpiresAt(Date.from(nowPlusTimeTimeRefresh.atZone(ZoneId.systemDefault()).toInstant()))
                .withIssuer(url)
                .sign(algorithm);

        return refreshToken;
    }

    public String montaAccessToken(String url, String username, List<String> roles) {

        Algorithm algorithm = Algorithm.HMAC256(jwtConfigProperties.getSecret().getBytes());
        LocalDateTime nowPlusTimeAccess = LocalDateTime.now().plusMinutes(jwtConfigProperties.getTimeAccess());

        String accesToken = JWT.create()
                .withSubject(username)
                .withClaim("roles", roles)
                .withExpiresAt(Date.from(nowPlusTimeAccess.atZone(ZoneId.systemDefault()).toInstant()))
                .withIssuer(url)
                .sign(algorithm);

        return accesToken;
    }

    public DecodedJWT recuperaDecodeJTW(String authorizationHeader) {

        DecodedJWT decodedJWT = null;

        if (authorizationHeader != null &&
                authorizationHeader.startsWith(BEARER_)) {

            Algorithm algorithm = Algorithm.HMAC256(jwtConfigProperties.getSecret().getBytes());
            String token = authorizationHeader.substring(BEARER_.length());
            JWTVerifier verifier = JWT.require(algorithm).build();
            decodedJWT = verifier.verify(token);
        }

        return decodedJWT;
    }

    public Map<String, String> montaMapToken(UserDetails userDetails, String url, boolean geraRefreshToken) {

        List<String> roles = userService.getRoles(userDetails.getAuthorities());

        String accessToken = montaAccessToken(url, userDetails.getUsername(), roles);
        String refreshToken = null;

        if(geraRefreshToken){
            refreshToken = montaRefreshToken(url, userDetails.getUsername());
        }

        Map<String, String> mapToken = new HashMap<>();

        boolean montouToken = false;

        if(!ObjectUtils.isEmpty(accessToken)){
            mapToken.put(jwtConfigProperties.getAccessToken(), accessToken);
            montouToken = true;
        }

        if(!ObjectUtils.isEmpty(refreshToken)){
            mapToken.put(jwtConfigProperties.getRefreshToken(), refreshToken);
            montouToken = true;
        }

        if(montouToken){
            mapToken.put("timestamp", DateUtils.localDateTimeToString(LocalDateTime.now()));
        }

        return mapToken;
    }
}
