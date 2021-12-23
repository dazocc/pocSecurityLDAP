package br.com.dazo.poc.security.filter;

import br.com.dazo.poc.security.service.JWTService;
import br.com.dazo.poc.utils.ResponseUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import br.com.dazo.poc.utils.CollectionsUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    public static final List<String> URL_LIBERADAS = CollectionsUtils.montaLista("/login", "/refresh/token");
    public static final String ROLES = "roles";
    public static final String AUTHORIZATION_NOT_FOUND = "Authorization not found";

    private final JWTService jtwService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(URL_LIBERADAS.contains(request.getServletPath())){
            filterChain.doFilter(request, response);
        }else {

            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            DecodedJWT decodedJWT = jtwService.recuperaDecodeJTW(authorizationHeader);

            if(decodedJWT != null){
                extraiInformacoesJWT(request, response, filterChain, decodedJWT);
            }else {
                ResponseUtils.escreveErrorResponse(response, AUTHORIZATION_NOT_FOUND, HttpStatus.FORBIDDEN);
            }
        }
    }

    private void extraiInformacoesJWT(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, DecodedJWT decodedJWT) throws ServletException, IOException {

        String username = decodedJWT.getSubject();
        String [] roles = decodedJWT.getClaim(ROLES).asArray(String.class);

        List<SimpleGrantedAuthority> authorities = Arrays.stream(roles)
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

}
