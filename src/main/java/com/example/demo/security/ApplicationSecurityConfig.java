package com.example.demo.security;

import com.example.demo.auth.ApplicationUserService;
import com.example.demo.jwt.JWTConfig;
import com.example.demo.jwt.JWTTokenVerifier;
import com.example.demo.jwt.JwtUsernameAndPasswordFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

//import static com.example.demo.security.ApplicationUserPermission.*;


@Configuration
@EnableWebSecurity
// to enable @PreAuthorize
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;
    private final SecretKey secretKey;
    private final JWTConfig jwtConfig;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService, SecretKey secretKey, JWTConfig jwtConfig) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("Security configure");
        // no need to input credentials for antMatchers
        // csrf, disable to enable post
        // http.csrf().disable().authorizeRequests().antMatchers("/", "index", "/css/*",
        // "/js/*").permitAll()
        // .antMatchers("/api/**").hasAnyRole(ApplicationUserRoles.STUDENT.name())
        // // .antMatchers(HttpMethod.DELETE,
        // // "/management/api/**").hasAnyAuthority(COURSE_WRITE.getPermission())
        // already
        // // use PreAuthorize
        // // .antMatchers(HttpMethod.POST,
        // // "/management/api/**").hasAnyAuthority(COURSE_WRITE.getPermission())
        // // .antMatchers(HttpMethod.PUT,
        // // "/management/api/**").hasAnyAuthority(COURSE_WRITE.getPermission())
        // // .antMatchers(HttpMethod.GET,
        // "/management/api/**").hasAnyRole(ADMIN.name(),
        // // ADMIN TRAINEE.name())
        // .anyRequest().authenticated().and().httpBasic();
        //
        //Use form login
//        http.csrf().disable().authorizeRequests().antMatchers("/", "index", "/css/*", "/js/*").permitAll()
//                .antMatchers("/api/**").hasAnyRole(ApplicationUserRoles.STUDENT.name()).anyRequest().authenticated()
//                .and().formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/courses", true)
//                .passwordParameter("password").usernameParameter("username").and()
//                // extend
//                .rememberMe().tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21)).key("something")
//                .rememberMeParameter("remember-me").and().logout().logoutUrl("/logout").clearAuthentication(true)
//                .invalidateHttpSession(true).deleteCookies("JSESSIONID", "remember-me").logoutSuccessUrl("/login");
        // .httpBasic();
        //Use jwt login
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilter(new JwtUsernameAndPasswordFilter(authenticationManager(), jwtConfig, secretKey))
                .addFilterAfter(new JWTTokenVerifier(secretKey, jwtConfig), JwtUsernameAndPasswordFilter.class)
                .authorizeRequests().antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasAnyRole(ApplicationUserRoles.STUDENT.name()).anyRequest().authenticated();
    }

    // create dummy user
//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails annBuilder = User.builder().username("Anna").password(passwordEncoder.encode("password"))
//                // .roles(ApplicationUserRoles.STUDENT.name())
//                .authorities(STUDENT.getGrantedAuth()).build();
//
//        UserDetails admin = User.builder().username("Linda").password(passwordEncoder.encode("password2"))
//                // .roles(ApplicationUserRoles.ADMIN.name())
//                .authorities(ADMIN.getGrantedAuth()).build();
//        UserDetails adminTrainee = User.builder().username("Tom").password(passwordEncoder.encode("password2"))
//                // .roles(ApplicationUserRoles.ADMIN TRAINEE.name())
//                .authorities(ADMINTRAINEE.getGrantedAuth()).build();
//
//        return new InMemoryUserDetailsManager(annBuilder, admin, adminTrainee);
//    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(applicationUserService);

        return daoAuthenticationProvider;

    }

}
