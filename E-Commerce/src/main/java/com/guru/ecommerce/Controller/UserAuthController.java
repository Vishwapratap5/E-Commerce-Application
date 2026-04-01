package com.guru.ecommerce.Controller;

import com.guru.ecommerce.Payload.UserLoginRequestDTO;
import com.guru.ecommerce.Payload.UserLoginResponseDTO;
import com.guru.ecommerce.Payload.UserRegisterRequestDTO;
import com.guru.ecommerce.Payload.UserResponseDTO;
import com.guru.ecommerce.Security.JwtService;
import com.guru.ecommerce.Service.UserAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserAuthController {
    @Autowired
    private UserAuthService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/auth/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRegisterRequestDTO customer){
        UserResponseDTO newUser=userService.localCustomerRegistration(customer);
        return new  ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<UserLoginResponseDTO> login(
            @Valid @RequestBody UserLoginRequestDTO request) {

        Authentication authRequest =
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),   // or email (be consistent!)
                        request.getPassword()
                );

        Authentication authenticated = authenticationManager.authenticate(authRequest);

        SecurityContextHolder.getContext().setAuthentication(authenticated);

        String jwt = jwtService.generateToken(authenticated);

        UserLoginResponseDTO response = new UserLoginResponseDTO(
                jwt,
                "Bearer",
                jwtService.getJwtExpiration()
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .body(response);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<String> logout(){
       SecurityContextHolder.clearContext();
       return  ResponseEntity.ok().body("Logged out...");
    }

    @GetMapping("/auth/username")
    public ResponseEntity<String> getUserName(){
        String UserName=userService.getCurrentUserName();
        return new ResponseEntity<>(UserName, HttpStatus.OK);
    }

    @GetMapping("/auth/user")
    public ResponseEntity<UserResponseDTO> getUser(){
        UserResponseDTO currentUser=userService.getCurrentUser();
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }
}
