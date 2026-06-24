package com.projectx.customer_support_api.auth;

import com.projectx.customer_support_api.customer.Customer;
import com.projectx.customer_support_api.customer.CustomerRepository;
import com.projectx.customer_support_api.customer.Role;
import com.projectx.customer_support_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        Customer customer = Customer.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.username())
                .role(Role.ROLE_CUSTOMER)
                .build();

        customerRepository.save(customer);

        String jwtToken = jwtService.generateToken(customer);
        return new AuthenticationResponse(jwtToken);

    }

    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        Customer customer = customerRepository.findByEmail(request.username())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        String jwtToken = jwtService.generateToken(customer);
        return new AuthenticationResponse(jwtToken);
    }
}