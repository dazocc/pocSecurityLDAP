package br.com.dazo.poc.security.controller;

import br.com.dazo.poc.security.service.RefreshTokenService;
import br.com.dazo.poc.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/refresh")
public class RefreshTokenController {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @GetMapping("/token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {

        try {

            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String url = request.getRequestURL().toString();

            Map<String, String> mapToken = refreshTokenService.refreshToken(authorizationHeader, url);

            if (mapToken != null) {
                ResponseUtils.escreveResponse(response, mapToken, HttpStatus.OK);
            } else {
                ResponseUtils.escreveErrorResponse(response, "ApplicationUser not found", HttpStatus.FORBIDDEN);
            }

        } catch (Exception e) {
            ResponseUtils.escreveErrorResponse(response, e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

}