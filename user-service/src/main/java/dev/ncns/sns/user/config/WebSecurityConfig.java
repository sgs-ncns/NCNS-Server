package dev.ncns.sns.user.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] PUBLIC_URLS = {
            "/", "/api/**"
    };
    private static final String[] WEB_URLS = {
            "error", "/h2-console/**", "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs", "/webjars/**"
    };

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .and().ignoring().mvcMatchers("/image/**")
                .and().ignoring().mvcMatchers(WEB_URLS);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable().headers().frameOptions().disable()
                .and()
                .authorizeRequests().mvcMatchers(PUBLIC_URLS).permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // session 생성x, 사용x
    }

}
