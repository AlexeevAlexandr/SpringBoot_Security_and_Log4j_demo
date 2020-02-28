package com.demo.security;

import com.demo.entity.Admin;
import com.demo.entity.Customer;
import com.demo.repository.AdminRepository;
import com.demo.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(name -> {
            if (name.equalsIgnoreCase("admin")){
                Admin admin = adminRepository.findByName("admin");
                return new org.springframework.security.core.userdetails.User(
                        passwordEncoder().encode(admin.getName()),
                        passwordEncoder().encode(admin.getPassword()),
                        true, true, true, true,
                        AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
            } else {
                Customer customer = customerRepository.findByName(name);
                if (customer != null) {
                    return new org.springframework.security.core.userdetails.User(
                            passwordEncoder().encode(customer.getName()),
                            passwordEncoder().encode(customer.getPassword().getPassword()),
                            true, true, true, true,
                            AuthorityUtils.createAuthorityList("ROLE_USER"));

                } else {
                    throw new UsernameNotFoundException("Could not find the user '" + name + "'");
                }
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/customer/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.POST, "/customer").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/customer").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/customer/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/admin").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/admin").hasRole("ADMIN")
                .and()
                .csrf().disable();
    }

    private PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
