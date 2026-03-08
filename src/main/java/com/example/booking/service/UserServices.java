package com.example.booking.service;


import com.example.booking.entity.User;
import com.example.booking.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServices {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    UserServices(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user){
        String encodedPassword =
                passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }
}
