package spring.iam.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import spring.iam.exception.JwtAuthenticationEntryPoint;
import spring.iam.filter.DaoAuthenticationFlt;
import spring.iam.service.IUserSrv;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@CrossOrigin
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSecurityConfigurer {
  @NonFinal
  String[] publicEndpoints = {
    "/api/v1/auth/sign-in", "/api/v1/auth/sign-up", "/v3/api-docs/**", "/swagger-ui/**",
  };

  @NonFinal
  @Value("${jwt.access-token.secret}")
  String accessTokenSecret;

  JwtAuthenticationEntryPoint entryPoint;
  DaoAuthenticationFlt daoAuthenticationFlt;
  IUserSrv userSrv;
  PasswordEncoder passwordEncoder;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf(AbstractHttpConfigurer::disable);
    httpSecurity.authorizeHttpRequests(
        request ->
            request.requestMatchers(publicEndpoints).permitAll().anyRequest().authenticated());
    httpSecurity.sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    httpSecurity
        .authenticationProvider(securityProvider())
        .addFilterBefore(daoAuthenticationFlt, UsernamePasswordAuthenticationFilter.class);
    httpSecurity.httpBasic(basicConfigurer -> basicConfigurer.authenticationEntryPoint(entryPoint));
    return httpSecurity.build();
  }

  @Bean
  public AuthenticationProvider securityProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userSrv.userDetailsService());
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
  }
}
