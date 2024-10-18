//package kolya.study.bookservice.config;
//
//import kolya.study.bookservice.service.BookUserDetailsService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
////import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
//import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
//import org.springframework.security.oauth2.core.oidc.user.OidcUser;
//import org.springframework.security.web.SecurityFilterChain;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Stream;
//
//@Configuration
//public class SecurityConfigOAuth {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(request -> {
//                    request.requestMatchers("/books/add-book").hasRole("ADMIN");
//                    request.requestMatchers("/user-profile/**").authenticated();
//                    request.anyRequest().permitAll();
//                })
//
//                .oauth2Login(oauth2 ->
//                        oauth2
//                                .loginPage("/oauth2/authorization/keycloak") // Налаштування кастомної сторінки логіну
//                                .defaultSuccessUrl("/books/add-book", true) // Переадресація після успішного входу
//                                .failureUrl("/oauth2/authorization/keycloak") // Переадресація при помилці входу
//                )
//                .logout(logout ->
//                        logout
//                                .logoutSuccessUrl("/")  // URL після виходу
//                                .logoutUrl("/logout")    // URL для ініціації logout
//                                .addLogoutHandler((request, response, auth) -> {
//                                    String logoutUri = "http://localhost:8080/realms/book-service/protocol/openid-connect/logout";
//                                    String redirectUri = "http://localhost:8081/books/add-book";  // або URL головної сторінки
//                                    OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) auth;
//                                    OidcUser oidcUser = (OidcUser) authToken.getPrincipal();
//                                    String idToken = oidcUser.getIdToken().getTokenValue();
//
//                                    try {
//                                        response.sendRedirect(logoutUri + "?post_logout_redirect_uri=" + redirectUri + "&id_token_hint=" + idToken);
//                                    } catch (IOException e) {
//                                        throw new RuntimeException(e);
//                                    }
//                                })
//                )
//                .build();
//
//    }
//
//    @Bean
//    public OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService() {
//        OidcUserService oidcUserService = new OidcUserService();
//        return userRequest -> {
//            OidcUser oidcUser = oidcUserService.loadUser(userRequest);
//            List<GrantedAuthority> authorities =
//                    Stream.concat(oidcUser.getAuthorities().stream(),
//                                    Optional.ofNullable(oidcUser.getClaimAsStringList("groups"))
//                                            .orElseGet(List::of)
//                                            .stream()
//                                            .filter(role -> role.startsWith("ROLE_"))
//                                            .map(SimpleGrantedAuthority::new)
//                                            .map(GrantedAuthority.class::cast))
//                            .toList();
//
//            return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
//        };
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(BookUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService);
//        authenticationProvider.setPasswordEncoder(passwordEncoder);
//        return new ProviderManager(authenticationProvider);
//    }
//}
