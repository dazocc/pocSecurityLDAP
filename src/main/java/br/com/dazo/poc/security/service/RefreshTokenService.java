package br.com.dazo.poc.security.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RefreshTokenService {

    public static final String AUTHORIZATION_NOT_FOUND = "Authorization not found";

    @Autowired
    private FakeStaticApplicactionUserService userService;

    @Autowired
    private JWTService jtwService;

    public Map<String, String> refreshToken(String authorizationHeader, String url) {

        Map<String, String> mapToken = null;

        DecodedJWT decodedJWT = jtwService.recuperaDecodeJTW(authorizationHeader);

        if (decodedJWT != null) {
            String username = decodedJWT.getSubject();

//            User user = (User) userService.loadUserByUsername(username);
//
//            if (user != null) {
//
//                mapToken = jtwService.montaMapToken(user, url, false);
//            }
        } else {
            throw new RuntimeException(AUTHORIZATION_NOT_FOUND);
        }

        return mapToken;
    }

}
