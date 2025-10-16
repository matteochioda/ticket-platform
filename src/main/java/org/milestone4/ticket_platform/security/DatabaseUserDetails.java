package org.milestone4.ticket_platform.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.milestone4.ticket_platform.model.Ruolo;
import org.milestone4.ticket_platform.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class DatabaseUserDetails implements UserDetails {

    private String username;

    private String password;

    private Set<GrantedAuthority> authorities;

    public DatabaseUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = new HashSet<>();
        for (Ruolo ruolo : user.getRuoli()) {
            SimpleGrantedAuthority sGA = new SimpleGrantedAuthority(ruolo.getNome());
            this.authorities.add(sGA);
            System.out.println("Aggiunta authority: " + sGA.getAuthority());
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

}
