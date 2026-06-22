package com.example.bank.Config;

import com.example.bank.Service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {


    private final MyUserDetailsService myUserDetailsService;
    private final MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    public SecurityConfiguration(MyUserDetailsService myUserDetailsService, MyAuthenticationSuccessHandler myAuthenticationSuccessHandler){
        this.myUserDetailsService = myUserDetailsService;
        this.myAuthenticationSuccessHandler = myAuthenticationSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception
    {
        return http
                .csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/login","/register").permitAll()
                        .requestMatchers("/homeadmin/**").hasAnyAuthority("ADMIN")
                        .requestMatchers("/homeuser/**").hasAnyAuthority("USER","ADMIN")
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin.
                        loginPage("/login").permitAll()
                        .successHandler(myAuthenticationSuccessHandler)
                        .failureUrl("/login?error=true"))
                .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll())
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        //authProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        authProvider.setUserDetailsService(myUserDetailsService);

        return authProvider;
    }
}
