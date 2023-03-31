package com.cwz.electronic.store.controllers;

import com.cwz.electronic.store.Security.JwtHelper;
import com.cwz.electronic.store.dtos.JwtRequest;
import com.cwz.electronic.store.dtos.JwtResponse;
import com.cwz.electronic.store.dtos.UserDto;
import com.cwz.electronic.store.exception.BadApiRequest;
import com.cwz.electronic.store.services.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtHelper helper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        this.doAuthenticate(request.getEmail(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .user(userDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);

        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadApiRequest("Invalid Username or Password.");
        }
    }

    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        String name = principal.getName();
        UserDto map = modelMapper.map(userDetailsService.loadUserByUsername(name), UserDto.class);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
