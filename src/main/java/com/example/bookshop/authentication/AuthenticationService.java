package com.example.bookshop.authentication;

import com.example.bookshop.authentication.request.AuthenticateRequest;
import com.example.bookshop.authentication.request.RegisterRequest;
import com.example.bookshop.authentication.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest registerRequest);

    AuthenticationResponse authenticate(AuthenticateRequest authenticateRequest);
}
