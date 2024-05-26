package kr.hs.gbsw.schoolpaper.config;

import kr.hs.gbsw.schoolpaper.jwt.JwtUtils;
import kr.hs.gbsw.schoolpaper.jwt.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig implements EnvironmentAware {

    private final List<AuthenticationProvider> authProviders = new ArrayList<>(); // 초기화 추가

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    public void registerProvider(AuthenticationManagerBuilder authenticationManagerBuilder) {
        if (authProviders != null) {
            authProviders.forEach(authenticationManagerBuilder::authenticationProvider);
        }
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration, JwtUtils jwtUtils) throws Exception {
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        http.csrf(csrf -> csrf.disable())
                .addFilterBefore(authenticationFilter(authenticationManager(authenticationConfiguration), jwtUtils), UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/users/**").permitAll()
                        .anyRequest().authenticated());

        if (authProviders != null) {
            authProviders.forEach(http::authenticationProvider);
        }

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        return new JwtAuthenticationFilter(authenticationManager, jwtUtils);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
