package com.desafio.magalu.security;

import com.desafio.magalu.security.jwt.JwtAuthenticationFilter;
import com.desafio.magalu.security.jwt.JwtAuthorizationFilter;
import com.desafio.magalu.security.jwt.handler.AccessDeniedHandler;
import com.desafio.magalu.security.jwt.handler.UnauthorizedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("userDetailService")
    private UserDetailsService userDetailsService;

    @Autowired
    private UnauthorizedHandler unauthorizedHandler;

    @Autowired
    private AccessDeniedHandler accesDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationManager authManager = authenticationManager();

        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/client/login/").permitAll()
                .antMatchers(HttpMethod.POST, "/api/client/").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable()
                .addFilter(new JwtAuthenticationFilter(authManager))
                .addFilter(new JwtAuthorizationFilter(authManager, userDetailsService))
                .exceptionHandling()
                .accessDeniedHandler(accesDeniedHandler)//os erros de nao puder ser autenticado cairão para AccessDeniedHandler
                .authenticationEntryPoint(unauthorizedHandler)//os erros de não autorizados cairão para dentro da UnauthorizedHandler
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //desliga os cookies da sessão
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       // BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        auth.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
}
