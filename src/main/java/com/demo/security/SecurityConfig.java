package com.demo.security;

import com.demo.entity.Customer;
import com.demo.repository.CustomerRepository;
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
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomerRepository customerRepository;

    @Autowired
    public SecurityConfig(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(name -> {
            Customer customer = customerRepository.findByName(name);
            if (customer != null) {
                if (customer.getName().equalsIgnoreCase("ADMIN")) {
                    return new org.springframework.security.core.userdetails.User(
                            passwordEncoder().encode(customer.getName()),
                            passwordEncoder().encode(customer.getPassword().getPassword()),
                            true, true, true, true,
                            AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
                } else {
                    return new org.springframework.security.core.userdetails.User(
                            passwordEncoder().encode(customer.getName()),
                            passwordEncoder().encode(customer.getPassword().getPassword()),
                            true, true, true, true,
                            AuthorityUtils.createAuthorityList("ROLE_USER"));
                }
            } else{
                throw new UsernameNotFoundException("Could not find the user '" + name + "'");
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
                .and()
                .csrf().disable();
    }

    private PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
