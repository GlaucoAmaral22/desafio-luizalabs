package com.desafio.magalu.security;

import com.desafio.magalu.repository.user.UserEntity;
import com.desafio.magalu.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service(value = "userDetailService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRep) {
        this.userRep = userRep;
    }

    private UserRepository userRep;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userLogado = userRep.findByEmail(email).get();

        if(Objects.isNull(userLogado)){
            throw new UsernameNotFoundException("User Not Found.");
        }
        return userLogado;
    }
}
