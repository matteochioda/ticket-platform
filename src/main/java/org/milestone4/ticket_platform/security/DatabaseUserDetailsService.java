package org.milestone4.ticket_platform.security;

import java.util.Optional;

import org.milestone4.ticket_platform.model.User;
import org.milestone4.ticket_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            return new DatabaseUserDetails(userOpt.get());
        } else {
            throw new UsernameNotFoundException("L'Username non è corretto");
        }
        
    }

}
