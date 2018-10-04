package com.example.pictweet.buisiness.repository;

import com.example.pictweet.buisiness.domain.Tweet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

    List<Tweet> findAllByOrderByIdDesc();

    Page<Tweet> findAllByOrderByIdDesc(Pageable pageable);
}
