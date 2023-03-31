package com.cwz.electronic.store.configuration;

import com.cwz.electronic.store.Security.JwtAuthenticationEntryPoint;
import com.cwz.electronic.store.Security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private JwtAuthenticationFilter authenticationFilter;

    private final String[] PUBLIC_URLS={
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-resources/**",
            "/v3/api-docs",
            "/v2/api-docs"
    };
//    @Bean
//    public UserDetailsService userDetailsService(){
//
//        //In Memory config
//        UserDetails normalUser = User.builder()
//                .username("Ziaul")
//                .password(passwordEncoder().encode("Abc123"))
//                .roles("Normal")
//                .build();
//
//        UserDetails adminUser = User.builder()
//                .username("Zeshan")
//                .password(passwordEncoder().encode("Abc123"))
//                .roles("Admin")
//                .build();
//
//        return new InMemoryUserDetailsManager(normalUser,adminUser);
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .formLogin()
//                .loginPage("login.html")
//                .loginProcessingUrl("/process-url")
//                .defaultSuccessUrl("/dashboard")
//                .failureUrl("error")
//                .and()
//                .logout()
//                .logoutUrl("/do-logout");

//        http
//                .csrf().disable()
//                .cors().disable()
//                .authorizeRequests().anyRequest().authenticated()
//                .and()
//                .httpBasic();
        http
                .csrf().disable()
                .cors().disable()
                .authorizeRequests()
                .antMatchers("/auth/login")
                .permitAll()
                .antMatchers(PUBLIC_URLS)
                .permitAll()
                .antMatchers(HttpMethod.POST,"/users/create")
                .permitAll()
                .antMatchers(HttpMethod.DELETE,"/users/delete/**")
                .hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
