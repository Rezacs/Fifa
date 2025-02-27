package Unipi.Fifa.config;


import Unipi.Fifa.services.NeoUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
     private final NeoUserDetailService neoUserDetailService;

     public SecurityConfig(NeoUserDetailService neoUserDetailService) {
         this.neoUserDetailService = neoUserDetailService;
     }

     @Bean
     SecurityFilterChain configure(HttpSecurity http, HttpSecurity httpSecurity) throws Exception {
         return httpSecurity
                 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .csrf(AbstractHttpConfigurer::disable)
                 .cors(Customizer.withDefaults())
                 .authorizeHttpRequests(auth -> auth.requestMatchers(
                                         "api/v1/auth/me","api/v1/enrolments/**"
                                 ).authenticated()
                                 .anyRequest().permitAll()
                 ).userDetailsService(neoUserDetailService)
                 .httpBasic(Customizer.withDefaults())
                 .build();
     }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
