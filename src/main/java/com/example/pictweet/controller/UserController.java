package com.example.pictweet.controller;

import com.example.pictweet.buisiness.domain.User;
import com.example.pictweet.buisiness.repository.UserRepository;
import com.example.pictweet.util.UserCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @ModelAttribute(name = "login_user")
    public UserDetails setLoginUser(@AuthenticationPrincipal UserCustom userCustom){
        return userCustom;
    }

    @RequestMapping(value = "/user/registration", method = RequestMethod.GET)
    public ModelAndView registration(ModelAndView mav){
        mav.addObject("user", new User());
        mav.setViewName("/user/registration");
        return mav;
    }

    @RequestMapping(value = "/user/registration", method = RequestMethod.POST)
    public ModelAndView createUser(@ModelAttribute("user") User newUser, ModelAndView mav){
        if (!newUser.getPassword().equals(newUser.getPasswordConfirmation())){
            mav.setViewName("redirect:/user/registration");
            return mav;
        }
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        userRepository.save(newUser);
        mav.setViewName("redirect:/user/login");
        return mav;
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.GET)
    public ModelAndView login(ModelAndView mav){
        mav.addObject("user", new User());
        mav.setViewName("/user/login");
        return mav;
    }
}
