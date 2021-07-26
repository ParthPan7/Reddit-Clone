package com.example.redditclone.security;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.annotation.PostConstruct;

import org.bouncycastle.cert.CertException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.example.redditclone.execptions.RedditException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.IOException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtProvider 
{
	private KeyStore keyStore;
	
	@PostConstruct
	public void init()
	{		   
			try 
			{	
				keyStore  = KeyStore.getInstance("JKS");
				InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
				keyStore.load(resourceAsStream,"secret".toCharArray());
			} 
			catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | java.io.IOException e) 
			{
				
				throw new RedditException("Exception occured while loading keystore"+ e);
			}	
	}
	public String generateToken(Authentication authentication) 
	{
		assert (null!=authentication);
		org.springframework.security.core.userdetails.User principle = (User) authentication.getPrincipal();
		log.info("[ principle ] user name : " + principle.getUsername());
		return Jwts.builder()
					.setSubject(principle.getUsername())
					.signWith(getPrivateKey())
					.compact();
	}
	
	private PrivateKey getPrivateKey() 
	{
		try 
		{
			return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
		} 
		catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) 
		{
			throw new RedditException("Exception occured while retrieving public key from keystore");
		} 
	}	
	
	public boolean validateToken(String jwt)
	{
		Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
		return true;
	}
	
	private PublicKey getPublicKey() 
	{
		try 
		{
			return keyStore.getCertificate("springblog").getPublicKey();
		} 
		catch (KeyStoreException e) 
		{
			throw new RedditException("Exception occured while retrieving public key from keystore");
		}
	}
	
	public String getUserNameFromJwt(String token)
	{
		Claims claims  = Jwts.parser()
						 .setSigningKey(getPublicKey())
						 .parseClaimsJws(token)
						 .getBody();
		
		return claims.getSubject();
	}
}
