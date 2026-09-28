package com.iosProject.iosProject.service.auth;

import com.iosProject.iosProject.bo.customUserDetails.CustomUserDetails;
import com.iosProject.iosProject.entity.UserEntity;
import com.iosProject.iosProject.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.info("Starting loadUserByUsername");
        try {
            log.info("Completed loadUserByUsername");
            return buildCustomUserDetailsOfUsername(s);
        } catch (NotFoundException e) {
            log.error("Failed loadUserByUsername", e);
            throw new RuntimeException(e);
        }
    }

    private CustomUserDetails buildCustomUserDetailsOfUsername(String username) throws NotFoundException {
        log.info("Starting buildCustomUserDetailsOfUsername");
        UserEntity user = userRepository.findByUsername(username).orElseThrow();
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setId(user.getId());
        userDetails.setUserName(user.getUsername());
        userDetails.setPassword(user.getPassword());
        userDetails.setRole(user.getRole().getTitle().name());
        log.info("Completed buildCustomUserDetailsOfUsername");
        return userDetails;
    }
}
