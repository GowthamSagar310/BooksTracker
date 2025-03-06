package com.gs310.bookstracker.account;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {

        // check if the identifier is an email or username
        String identifierType = identifier.contains("@") ? "email" : "username";
        UserEntity userEntity = userRepository.findByUsername(identifier).or(() -> userRepository.findByEmail(identifier)).orElseThrow(
                () -> new UsernameNotFoundException("User not found with " + identifierType + " : " + identifier)
        );

        // convert the account into UserDetails / User
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(userEntity.getRole())); // roles
        return new User(userEntity.getEmail(), userEntity.getPassword(), authorities);
    }

    // loadUserByEmail is not available in UserDetailsService



}
