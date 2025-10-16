package org.milestone4.ticket_platform.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http)
    throws Exception {

        http.authorizeHttpRequests()
            .requestMatchers("/giullstravells/tickets/create", "/giullstravells/tickets/edit/**").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.POST, "/giullstravells/tickets/**").hasAnyAuthority("OPERATORE", "ADMIN")
            .requestMatchers("/giullstravells/categorie", "/giullstravells/categorie/**").hasAuthority("ADMIN")
            .requestMatchers("/giullstravells/tickets", "/giullstravells/tickets/**").hasAnyAuthority("OPERATORE", "ADMIN")
            .requestMatchers("/**").permitAll()
            .and().formLogin()
            .and().logout();
        return http.build();
    }


    @Bean
    DatabaseUserDetailsService userDetailsService() {
        return new DatabaseUserDetailsService();
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}
