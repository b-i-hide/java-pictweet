package com.example.pictweet.controller;

import com.example.pictweet.buisiness.domain.Tweet;
import com.example.pictweet.buisiness.domain.User;
import com.example.pictweet.buisiness.repository.TweetRepository;
import com.example.pictweet.buisiness.repository.UserRepository;
import com.example.pictweet.util.UserCustom;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TweetController {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;

    @ModelAttribute(name = "login_user")
    public UserDetails setLoginUser(@AuthenticationPrincipal UserCustom userCustom){
        return userCustom;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index(@PageableDefault(size = 5) Pageable pageable, ModelAndView mav, @AuthenticationPrincipal UserDetails userDetails){
        Page<Tweet> tweets = tweetRepository.findAllByOrderByIdDesc(pageable);
        mav.addObject("tweets", tweets);
        mav.addObject("login_user", userDetails);
        mav.setViewName("tweet/index");
        return mav;
    }

    @RequestMapping(value = "/tweet/new", method = RequestMethod.GET)
    public ModelAndView newTweet(ModelAndView mav) {
        mav.setViewName("tweet/new");
        return mav;
    }

    @RequestMapping(value = "/tweet/new", method = RequestMethod.POST)
    public ModelAndView createTweet(Tweet newTweet, @AuthenticationPrincipal UserCustom userCustom, ModelAndView mav){
        User user = userRepository.getOne(userCustom.getId());
        newTweet.setUser(user);
        tweetRepository.saveAndFlush(newTweet);
        mav.setViewName("tweet/create");
        return mav;
    }

    @RequestMapping(value = "/tweet/{id}/edit", method = RequestMethod.GET)
    public ModelAndView editTweet(@PathVariable("id") Long id, ModelAndView mav){
        Tweet tweet = tweetRepository.getOne(id);
        mav.addObject("tweet", tweet);
        mav.setViewName("/tweet/edit");
        return mav;
    }

    @RequestMapping(value = "/tweet/{id}/edit", method = RequestMethod.POST)
    public ModelAndView updateTweet(@ModelAttribute Tweet editTweet, @PathVariable("id") Long id, @AuthenticationPrincipal UserCustom userCustom, ModelAndView mav){
        Tweet tweet = tweetRepository.getOne(id);
        if (!tweet.getUser().getId().equals(userCustom.getId())){
            mav.setViewName("redirect:/tweet/" + id + "/edit");
            return mav;
        }
        BeanUtils.copyProperties(editTweet, tweet);
        tweetRepository.save(tweet);
        mav.setViewName("tweet/update");
        return mav;
    }

    @RequestMapping(value = "/tweet/{id}/delete", method = RequestMethod.DELETE)
    public ModelAndView deleteTweet(@PathVariable("id") Long id, @AuthenticationPrincipal UserCustom userCustom, ModelAndView mav) {
        Tweet tweet = tweetRepository.getOne(id);
        if (!tweet.getUser().getId().equals(userCustom.getId())) {
            mav.setViewName("redirect:/");
            return mav;
        }
        tweetRepository.delete(tweet);
        mav.setViewName("redirect:/");
        return mav;
    }

    @RequestMapping(value = "/tweet/{id}", method = RequestMethod.GET)
    public ModelAndView showTweet(@PathVariable("id") Long id, ModelAndView mav){
        Tweet tweet = tweetRepository.getOne(id);
        mav.addObject("tweet", tweet);
        mav.setViewName("tweet/show");
        return mav;
    }
}
