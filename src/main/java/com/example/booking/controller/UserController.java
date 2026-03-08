package com.example.booking.controller;


import com.example.booking.entity.LoginRequest;
import com.example.booking.entity.ResponseStructure;
import com.example.booking.entity.User;
import com.example.booking.repository.UserRepository;
import com.example.booking.service.UserServices;
import com.example.booking.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserServices userServices;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    UserController(UserServices userServices, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil){
        this.userServices = userServices;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/create")
    public User createUser(@RequestBody User user){
        return userServices.createUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseStructure<User>> login(@RequestBody LoginRequest request){

        ResponseStructure<User> response = new ResponseStructure<>();

        Optional<User> optionalUser =
                userRepository.findByUsername(request.getUsername());

        if(optionalUser.isEmpty()){
            response.setStatus(404);
            response.setMessage("User not found");
            response.setData(null);

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();

        if(!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())){

            response.setStatus(401);
            response.setMessage("Invalid credentials");
            response.setData(null);

            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        String token = jwtUtil.generateToken(user.getUsername());

        response.setStatus(200);
        response.setMessage("Login successful");
        user.setPassword("");
        response.setData(user);
        response.setToken(token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
