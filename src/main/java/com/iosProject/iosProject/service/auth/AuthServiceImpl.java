package com.iosProject.iosProject.service.auth;

import com.iosProject.iosProject.bo.auth.AuthenticationResponse;
import com.iosProject.iosProject.bo.auth.CreateLoginRequest;
import com.iosProject.iosProject.bo.auth.CreateSignupRequest;
import com.iosProject.iosProject.bo.auth.LogoutResponse;
import com.iosProject.iosProject.bo.customUserDetails.CustomUserDetails;
import com.iosProject.iosProject.config.JWTUtil;
import com.iosProject.iosProject.entity.RoleEntity;
import com.iosProject.iosProject.entity.UserEntity;
import com.iosProject.iosProject.repository.RoleRepository;
import com.iosProject.iosProject.repository.UserRepository;
import com.iosProject.iosProject.util.enums.Roles;
import com.iosProject.iosProject.util.exceptions.BodyGuardException;
import com.iosProject.iosProject.util.exceptions.UserNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsService userDetailService;

    private final JWTUtil jwtUtil;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailService, JWTUtil jwtUtil, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailService = userDetailService;
        this.jwtUtil = jwtUtil;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void signup(CreateSignupRequest createSignupRequest) {
        log.info("Starting signup");
        RoleEntity roleEntity = roleRepository.findRoleEntityByTitle(Roles.user.name()).orElseThrow(() -> new BodyGuardException("no Roles Found"));
        ;
        UserEntity user = new UserEntity();
        user.setName(createSignupRequest.getName());
        user.setUsername(createSignupRequest.getUsername());
        user.setEmail(createSignupRequest.getEmail());
        user.setRole(roleEntity);
        user.setPassword(bCryptPasswordEncoder.encode(createSignupRequest.getPassword()));
        log.info("Calling userRepository save");
        userRepository.save(user);
    }

    @Override
    public AuthenticationResponse login(CreateLoginRequest createLoginRequest) {
        log.info("Starting login");
        requiredNonNull(createLoginRequest.getUsername(), "username");
        requiredNonNull(createLoginRequest.getPassword(), "password");
        String username = createLoginRequest.getUsername().toLowerCase();
        String password = createLoginRequest.getPassword();
        authentication(username, password);
        log.info("Calling userDetailService loadUserByUsername");
        CustomUserDetails userDetails = userDetailService.loadUserByUsername(username);
        String accessToken = jwtUtil.generateToken(userDetails);
        AuthenticationResponse response = new AuthenticationResponse();
        response.setId(userDetails.getId());
        response.setUsername(userDetails.getUsername());
        response.setRole(userDetails.getRole());
        response.setToken("Bearer " + accessToken);
        log.info("Completed login");
        return response;
    }

    @Override
    public void logout(LogoutResponse logoutResponse) {
        log.info("Starting logout");
        requiredNonNull(logoutResponse.getToken(), "Token");
    }

    private void requiredNonNull(Object obj, String name) {
        if (obj == null || obj.toString().isEmpty()) {
            throw new BodyGuardException(name + "can not be empty");
        }
    }

    private void authentication(String username, String password) {
        log.info("Starting authentication");
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BodyGuardException e) {
            log.error("Failed authentication", e);
            throw new BodyGuardException("Incorrect password");
        } catch (AuthenticationServiceException e) {
            log.error("Failed authentication", e);
            throw new UserNotFoundException("Incorrect username");
        }
    }
}
