package org.example.uberprojectauthservice.controllers;

import org.example.uberprojectauthservice.dto.PassengerDto;
import org.example.uberprojectauthservice.dto.PassengerSignupRequestDto;
import org.example.uberprojectauthservice.services.authService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class authController {

     private authService service;

     public authController(authService service){
         this.service= service;
     }

    @PostMapping("/signup/passenger")
    public ResponseEntity<PassengerDto> signUp(@RequestBody PassengerSignupRequestDto passengerSignupRequestDto){
       PassengerDto respone =  service.signUpPassenger(passengerSignupRequestDto);
       return new ResponseEntity<>(respone , HttpStatus.CREATED);

    }

    @GetMapping("/signIn/passenger")
    public ResponseEntity<?> signIn(){
         return new ResponseEntity<>(10 , HttpStatus.OK);
    }



}
