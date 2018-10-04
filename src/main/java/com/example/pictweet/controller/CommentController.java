package com.example.pictweet.controller;

import com.example.pictweet.buisiness.domain.Comment;
import com.example.pictweet.buisiness.domain.Tweet;
import com.example.pictweet.buisiness.domain.User;
import com.example.pictweet.buisiness.repository.CommentRepository;
import com.example.pictweet.buisiness.repository.TweetRepository;
import com.example.pictweet.buisiness.repository.UserRepository;
import com.example.pictweet.util.UserCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    TweetRepository tweetRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/tweet/{tweetId}/comment", method = RequestMethod.POST)
    public ModelAndView createComment(@ModelAttribute Comment comment, @PathVariable("tweetId") Long tweetId, @AuthenticationPrincipal UserCustom userCustom, ModelAndView mav){
        Tweet tweet = tweetRepository.getOne(tweetId);
        User user = userRepository.getOne(userCustom.getId());
        comment.setTweet(tweet);
        comment.setUser(user);
        commentRepository.saveAndFlush(comment);
        mav.setViewName("redirect:/tweet/" + tweetId);
        return mav;
    }
}
