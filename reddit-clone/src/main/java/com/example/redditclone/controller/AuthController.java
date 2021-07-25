package com.example.redditclone.controller;

import com.example.redditclone.dto.AuthenticationResponse;
import com.example.redditclone.dto.LoginRequest;
import com.example.redditclone.dto.RegisterRequest;
import com.example.redditclone.execptions.RedditException;
import com.example.redditclone.service.AuthService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController
{
	private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) throws RedditException
    {
    	log.info("singup controller");
    	System.out.println("signup controller");
    	authService.signup(registerRequest);
    	return new ResponseEntity<>("Registration Successful!",HttpStatus.OK);
    }
    
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest)
    {
    	return authService.login(loginRequest);
    }
    
    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token)
    {
    	authService.verifyAccount(token);
    	return new ResponseEntity<>("Account activated Successfully", HttpStatus.OK);
    }

}
