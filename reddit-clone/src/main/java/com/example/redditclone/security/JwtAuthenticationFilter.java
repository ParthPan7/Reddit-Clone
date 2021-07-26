package com.example.redditclone.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

import org.springframework.util.StringUtils;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter 
{
	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private UserDetailsService userDetailsService;
	@Override
	protected void doFilterInternal(HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain) 
			throws ServletException, IOException 
	{
			String jwtTokenFromRequest = getJwtFromResponse(request);
			if (StringUtils.hasText(jwtTokenFromRequest) && jwtProvider.validateToken(jwtTokenFromRequest))
			{
				String username  = jwtProvider.getUserNameFromJwt(jwtTokenFromRequest);
				log.info("["+this.getClass().getName() +"]"+" username : "+username);
				UserDetails user = userDetailsService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = 
						new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			filterChain.doFilter(request, response);
	}

	private String getJwtFromResponse(HttpServletRequest request) 
	{
		String bearerToken = request.getHeader("Authorization");	
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
		{
			return bearerToken.substring(7);
		}
		return bearerToken;
	}
	
}
