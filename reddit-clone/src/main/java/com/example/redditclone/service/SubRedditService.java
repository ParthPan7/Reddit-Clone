package com.example.redditclone.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.redditclone.dto.SubredditDto;
import com.example.redditclone.model.SubReddit;
import com.example.redditclone.repository.SubredditRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class SubRedditService 
{
	private final SubredditRepository subredditRepository;
	
	@Transactional
	public SubredditDto save(SubredditDto subredditDto)
	{
		SubReddit save  = subredditRepository.save(mapSubredditDto(subredditDto));
		subredditDto.setId(save.getId());
		return subredditDto;
	}
	
	private SubReddit mapSubredditDto(SubredditDto subredditDto)
	{
		return SubReddit.builder().name(subredditDto.getName())
					.description(subredditDto.getDescription())
					.build();
		
	}

	@Transactional(readOnly = true)
	public  List<SubredditDto> getAllSubreddits() 
	{
			return subredditRepository.findAll()
					.stream()
					.map(this::mapToDto)
					.collect(Collectors.toList());
	}

	private SubredditDto mapToDto(SubReddit subreddit) 
	{
			return SubredditDto
					.builder()
					.name(subreddit.getName())
					.id(subreddit.getId())
					.numberOfPosts(subreddit.getPosts().size())
					.build();
	}
	
}
