package sk.stuba.fei.uim.dp.attendanceapi.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import sk.stuba.fei.uim.dp.attendanceapi.ui.LoginView;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Order(1)
    @Configuration
    public static class RestSecurityConfig{
        @Autowired
        private JwtAuthEntryPoint authEntryPoint;

        @Autowired
        MvcRequestMatcher.Builder mvc;
        @Bean
        public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
            http
                    .securityMatcher("/api/**")
                    .csrf(AbstractHttpConfigurer::disable)
                    .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(authEntryPoint))
                    .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests( auth -> {
                        auth.requestMatchers(
                                mvc.pattern("/api/auth/google"),
                                mvc.pattern("/api/auth/login"),
                                mvc.pattern("/api/auth/signup"),
                                mvc.pattern("/api/user/resetPassword")
                        ).permitAll();
                        auth.anyRequest().authenticated();
                    });
            http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
            return http.build();
        }
        @Bean
        public JWTAuthenticationFilter jwtAuthenticationFilter(){
            return new JWTAuthenticationFilter();
        }
    }

    @Configuration
    @Order(2)
    public static class WebSecurityConfiguration extends VaadinWebSecurity{
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(auth ->
                    auth.requestMatchers(
                            AntPathRequestMatcher.antMatcher("/images/*.png"),
                            AntPathRequestMatcher.antMatcher("/resetPassword")).permitAll()
            );
            super.configure(http);
            setLoginView(http, LoginView.class);
        }
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
