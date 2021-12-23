package br.com.dazo.poc.security.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FakeStaticApplicactionUserService /*implements UserDetailsService*/ {

//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        ApplicationUser userEncontrado = null;
//
//        if(!ObjectUtils.isEmpty(username)){
//            List<ApplicationUser> users = getApplicationUser();
//
//            userEncontrado = users.stream()
//                    .filter(user -> username.equals(user.getUsername()))
//                    .findFirst()
//                    .orElse(null);
//        }
//
//        return userEncontrado;
//    }
//
//    private List<ApplicationUser> getApplicationUser() {
//
//        List<ApplicationUser> users = new ArrayList<>();
//
//        ApplicationUser admin = ApplicationUser.builder()
//                .username("admin")
//                .password(passwordEncoder.encode("admin"))
//                .authorities(getRoles("ADMIN"))
//                .build();
//
//        ApplicationUser user = ApplicationUser.builder()
//                .username("user")
//                .password(passwordEncoder.encode("user"))
//                .authorities(getRoles("USER"))
//                .build();
//
//        users.add(admin);
//        users.add(user);
//
//        return users;
//    }
//
    public List<String> getRoles(Collection<? extends GrantedAuthority> authorities) {

        List<String> roles = null;

        if (!ObjectUtils.isEmpty(authorities)) {
            roles = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
        }

        return roles;
    }

    public List<GrantedAuthority> getRoles(String... roles) {

        List<GrantedAuthority> listAuthorities = null;

        if(!ObjectUtils.isEmpty(roles)){
            listAuthorities = Arrays.stream(roles)
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        }

        return listAuthorities;
    }
}
