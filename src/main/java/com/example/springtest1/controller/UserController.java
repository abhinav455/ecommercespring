package com.example.springtest1.controller;

import com.example.springtest1.dto.UserRequest;
import com.example.springtest1.dto.UserResponse;
import com.example.springtest1.model.User;
import com.example.springtest1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    //@Autowired
    private final UserService userService;

    //@GetMapping("/api/users")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        //return ResponseEntity.ok(userService.fetchAllUsers());
        //return ResponseEntity<>(userService.fetchAllUsers(), HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(userService.fetchAllUsers());

    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id){
        //ResponseEntity
       // HttpStatus
//        User user = userService.fetchUser(id);
//        if(user==null)
//                return ResponseEntity.notFound().build();
//        return ResponseEntity.ok(user);
        return userService.fetchUser(id).map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());

    }


    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest){
        userService.addUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(user.toString());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> createUser(@PathVariable Long id,
                                         @RequestBody User updatedUser){
        boolean updated = userService.updateUser(id, updatedUser);
        if(updated)
            return ResponseEntity.status(HttpStatus.OK).body("User added successfully");
        return ResponseEntity.notFound().build();
    }



}
