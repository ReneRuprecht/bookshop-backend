package com.example.bookshop.authentication;

import com.example.bookshop.authentication.exception.EmailAlreadyExistsException;
import com.example.bookshop.authentication.exception.UserNotFoundException;
import com.example.bookshop.authentication.request.AuthenticateRequest;
import com.example.bookshop.authentication.request.RegisterRequest;
import com.example.bookshop.authentication.response.AuthenticationResponse;
import com.example.bookshop.config.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationRepository authenticationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {

        if (this.authenticationRepository.findByEmail(registerRequest.email()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = User.builder()
                        .username(registerRequest.username())
                        .email(registerRequest.email())
                        .password(passwordEncoder.encode(registerRequest.password()))
                        .role(Role.USER)
                        .build();


        String token = this.jwtService.generateToken(user);

        this.authenticationRepository.save(user);

        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authenticate(AuthenticateRequest authenticateRequest) {
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticateRequest.email(),
                authenticateRequest.password()
        ));

        User user = authenticationRepository.findByEmail(authenticateRequest.email())
                                            .orElseThrow(() -> new UserNotFoundException("User not found"));

        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token);
    }
}
