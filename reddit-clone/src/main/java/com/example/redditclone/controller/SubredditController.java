package com.example.redditclone.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.redditclone.dto.SubredditDto;
import com.example.redditclone.service.SubRedditService;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/subreddit")
@Slf4j
@AllArgsConstructor
public class SubredditController 
{
	private final SubRedditService subRedditService;
	
	@PostMapping
	public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto)
	{
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(subRedditService.save(subredditDto));
	}
	
	@GetMapping
	public ResponseEntity<List<SubredditDto>> getAllSubreddits()
	{
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(subRedditService.getAllSubreddits());
	}
	
}
