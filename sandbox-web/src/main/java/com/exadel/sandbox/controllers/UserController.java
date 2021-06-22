package com.exadel.sandbox.controllers;

import com.exadel.sandbox.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/current")
    ResponseEntity<?> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return ResponseEntity.ok().body(userService.findByName(currentPrincipalName));
    }
    @PostMapping(produces = {"application/json"},
            consumes = {"application/json"},
           path = {"/addEvent/toOrder/{eventId}"})
    ResponseEntity<?> addEventToUserOrder(@PathVariable Long id){
        return ResponseEntity.ok().body(null);
    }

    @PostMapping(produces = {"application/json"},
            consumes = {"application/json"},
            path = {"/addEvent/toSaved/{eventId}"})
    ResponseEntity<?> addEventToSaved(@PathVariable Long id){
        return ResponseEntity.ok().body(null);
    }


}