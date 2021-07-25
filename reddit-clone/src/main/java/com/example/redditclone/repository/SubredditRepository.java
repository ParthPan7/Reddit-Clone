package com.example.redditclone.repository;

import com.example.redditclone.model.SubReddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubredditRepository extends JpaRepository<SubReddit, Long>
{
}
