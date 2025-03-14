package Unipi.Fifa.config;


import Unipi.Fifa.services.MongoUserDetailService;
import Unipi.Fifa.services.NeoUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
     private final MongoUserDetailService mongoUserDetailService;

     public SecurityConfig(NeoUserDetailService neoUserDetailService, MongoUserDetailService mongoUserDetailService) {
         this.neoUserDetailService = neoUserDetailService;
         this.mongoUserDetailService = mongoUserDetailService;
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
                 ).userDetailsService(mongoUserDetailService)
                 .httpBasic(Customizer.withDefaults())
                 .build();
     }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//                .userDetailsService(neoUserDetailService)
//                .passwordEncoder(passwordEncoder())
//                .and()
//                .build();
//    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        // Using AuthenticationManagerBuilder to configure AuthenticationManager
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(mongoUserDetailService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
