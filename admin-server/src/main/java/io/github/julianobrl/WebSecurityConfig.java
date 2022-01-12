package io.github.julianobrl;

import java.util.UUID;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import de.codecentric.boot.admin.server.config.AdminServerProperties;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final AdminServerProperties adminServer;

    public WebSecurityConfig(AdminServerProperties adminServer) {
        this.adminServer = adminServer;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = 
          new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(this.adminServer.getContextPath() + "/");

        http
            .authorizeRequests()
                .antMatchers(this.adminServer.getContextPath() + "/assets/**").permitAll()
                .antMatchers(this.adminServer.getContextPath() + "/login").permitAll()

                .antMatchers(this.adminServer.getContextPath() + "/instances").hasAnyRole("APP","ADMIN")
                .antMatchers(this.adminServer.getContextPath() + "/actuator/**").hasAnyRole("APP")
                
                .antMatchers(this.adminServer.getContextPath() + "/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage(this.adminServer.getContextPath() + "/login")
                .successHandler(successHandler)
                .failureForwardUrl(this.adminServer.getContextPath() + "/login")
                .and()
            .logout()
                .logoutUrl(this.adminServer.getContextPath() + "/logout")
                .and()
            .httpBasic()
                .and()
            .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers(
                  new AntPathRequestMatcher(this.adminServer.getContextPath() + 
                    "/instances", HttpMethod.POST.toString()), 
                  new AntPathRequestMatcher(this.adminServer.getContextPath() + 
                    "/instances/*", HttpMethod.DELETE.toString()),
                  new AntPathRequestMatcher(this.adminServer.getContextPath() + "/actuator/**"))
                .and()
            .rememberMe()
                .key(UUID.randomUUID().toString())
                .tokenValiditySeconds(1209600);
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.inMemoryAuthentication()
        .withUser("admin")
        .password("{noop}admin")
        .roles("ADMIN")
        
        .and()
        
        .withUser("app")
        .password("{noop}app")
        .roles("APP");
    }
    
    
}