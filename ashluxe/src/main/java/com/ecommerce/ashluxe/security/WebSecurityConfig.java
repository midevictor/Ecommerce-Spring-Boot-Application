package com.ecommerce.ashluxe.security;


import com.ecommerce.ashluxe.model.AppRole;
import com.ecommerce.ashluxe.model.Role;
import com.ecommerce.ashluxe.model.User;
import com.ecommerce.ashluxe.repositories.RoleRepository;
import com.ecommerce.ashluxe.repositories.UserRepository;
import com.ecommerce.ashluxe.security.jwt.AuthEntryPointJwt;
import com.ecommerce.ashluxe.security.jwt.AuthTokenFilter;
import com.ecommerce.ashluxe.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Set;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)requests.requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
//                        .requestMatchers("/api/public/**").permitAll()
//                        .requestMatchers("/api/admin/**").permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                .anyRequest()).authenticated());
        // Set the authentication provider
        http.authenticationProvider(authenticationProvider());
        // Add the JWT authentication filter
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(
                exception -> exception.authenticationEntryPoint(unauthorizedHandler) // Handle unauthorized access

        );
        // enable frame options to allow H2 console access
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        return http.build();

    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/v2/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/configuration/ui"
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder){
        return args -> {
            //Retrieve or create roles
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> {
                        Role newUserRole =  new Role(AppRole.ROLE_USER);
                        return roleRepository.save(newUserRole);
                    });
            Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                    .orElseGet(() -> {
                        Role newSellerRole =  new Role(AppRole.ROLE_SELLER);
                        return roleRepository.save(newSellerRole);
                    });
            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role newAdminRole =  new Role(AppRole.ROLE_ADMIN);
                        return roleRepository.save(newAdminRole);
                    });
            Set<Role> userRoles = Set.of(userRole);
            Set<Role> sellerRoles = Set.of(sellerRole);
            Set<Role> adminRoles = Set.of(userRole, sellerRole, adminRole);

            //Create default users, if not present
            if (!userRepository.existsByUsername("user1")){
                User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("password1"));
                // user1.setRoles(userRoles);
                userRepository.save(user1);
            }

            if (!userRepository.existsByUsername("seller1")) {
                User seller1 = new User("seller1", "seller1@example.com", passwordEncoder.encode("password2"));
                // seller1.setRoles(sellerRoles);
                userRepository.save(seller1);
            }
            if (!userRepository.existsByUsername("admin1")) {
                User admin1 = new User("admin1", "admin1@example.com", passwordEncoder.encode("password3"));
                // admin1.setRoles(adminRoles);
                userRepository.save(admin1);
            }

            //Update roles for existing users
            userRepository.findByUsername("user1").ifPresent(user -> {
                if (user.getRoles() == null || user.getRoles().isEmpty()) {
                    user.setRoles(userRoles);
                    userRepository.save(user);
                }
            });

            userRepository.findByUsername("seller1").ifPresent(seller -> {
                if (seller.getRoles() == null || seller.getRoles().isEmpty()) {
                    seller.setRoles(sellerRoles);
                    userRepository.save(seller);
                }
            });

            userRepository.findByUsername("admin1").ifPresent(admin -> {
                if (admin.getRoles() == null || admin.getRoles().isEmpty()) {
                    admin.setRoles(adminRoles);
                    userRepository.save(admin);
                }
            });

        };
    }
}
