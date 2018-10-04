package com.example.pictweet.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Set;

public class UserCustom extends User {
    private static final long serialVersionUID = 1L;
    private long id;

    public UserCustom(Long id, String username, String password, Set<? extends GrantedAuthority> authorities){
        super(username, password, authorities);
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
