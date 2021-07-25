package com.example.redditclone.service;

import com.example.redditclone.dto.AuthenticationResponse;
import com.example.redditclone.dto.LoginRequest;
import com.example.redditclone.dto.RegisterRequest;
import com.example.redditclone.execptions.RedditException;
import com.example.redditclone.model.NotificationEmail;
import com.example.redditclone.model.User;
import com.example.redditclone.model.VerificationToken;
import com.example.redditclone.repository.UserRepository;
import com.example.redditclone.repository.VerificationTokenRepository;
import com.example.redditclone.security.JwtProvider;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Transactional
@Slf4j
@Service
public class AuthService
{

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;
	
	public void signup(RegisterRequest request) throws RedditException
    {
       User user = new User();
       user.setUserName(request.getUsername());
       user.setPassword(passwordEncoder.encode(request.getPassword()));
       user.setEmail(request.getEmail());
       user.setEnabled(false);
       user.setCreated(Instant.now());
       userRepository.save(user);
       String token = generateVerificationToken(user);
       mailService.sendMail(new NotificationEmail("Please activate your account",
    		   user.getEmail(),
    		   "appreciate your efforts for signing up to Reddit Clone Service, "+
                "click the url to activate your account : "+
    				   "http://localhost:9085/api/auth/accountVerification/"+token));
    }

	private String generateVerificationToken(User user) 
	{
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		
		verificationTokenRepository.save(verificationToken);
		return token;
	}

	public void verifyAccount(String token) 
	{
		Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
		verificationToken.orElseThrow(() -> new RedditException("Invalid user" ));
		fetchUserAndActivateAccount(verificationToken.get());
	}

	@Transactional
	private void fetchUserAndActivateAccount(VerificationToken verificationToken) 
	{
		String userName = verificationToken.getUser().getUserName();
		User user = userRepository.findByUserName(userName).orElseThrow(()->new RedditException("can't found user : "+userName));
		user.setEnabled(true);
		userRepository.save(user);
	}

	public AuthenticationResponse login(LoginRequest loginRequest) 
	{
	  log.info("[login Service] username  : " + loginRequest.getUsername() + " password:"+loginRequest.getPassword());	
	  Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
	  log.info("generating authenticated request ..!");
	  SecurityContextHolder.getContext().setAuthentication(authenticate);
	  log.info("authenticated ..!");
	  String token = jwtProvider.generateToken(authenticate); 
	  log.info("Generated token" + token);
	  return new AuthenticationResponse(token, loginRequest.getUsername());
	}
	
}
