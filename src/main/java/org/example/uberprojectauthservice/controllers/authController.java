package org.example.uberprojectauthservice.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.uberprojectauthservice.dto.AuthRequestDto;
import org.example.uberprojectauthservice.dto.PassengerDto;
import org.example.uberprojectauthservice.dto.PassengerSignupRequestDto;
import org.example.uberprojectauthservice.services.AuthService;
import org.example.uberprojectauthservice.services.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class authController {

    @Value("${jwt.cookieExpiry}")
    private int cookieExpiry;
    private final AuthenticationManager authenticationManager;
    private final AuthService service;
    private final JwtService jwtService;

     public authController(AuthService service, AuthenticationManager authenticationManager , JwtService jwtService){
         this.service= service;
         this.jwtService = jwtService;
         this.authenticationManager = authenticationManager;
     }

    @PostMapping("/signup/passenger")
    public ResponseEntity<PassengerDto> signUp(@RequestBody PassengerSignupRequestDto passengerSignupRequestDto){
       PassengerDto respone =  service.signupPassenger(passengerSignupRequestDto);
       return new ResponseEntity<>(respone , HttpStatus.CREATED);

    }

    @PostMapping("/signin/passenger")
    public ResponseEntity<?> signIn(@RequestBody AuthRequestDto authRequestDto, HttpServletResponse response) {
        System.out.println("Request received " + authRequestDto.getEmail() + " " + authRequestDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword()));
        if(authentication.isAuthenticated()) {
            String token = jwtService.createToken(authRequestDto.getEmail());
            ResponseCookie cookie  = ResponseCookie.from("JwtToken" , token)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(cookieExpiry)
                    .build();
            response.setHeader(HttpHeaders.SET_COOKIE , cookie.toString());
            return new ResponseEntity<>(token, HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> getToken(HttpServletRequest request , HttpServletResponse response){
        System.out.println("this is working");
         Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println("Cookie Name: " + cookie.getName());
                System.out.println("Cookie Value: " + cookie.getValue());
            }
        } else {
            System.out.println("No cookies found");
        }

        return ResponseEntity.ok("success");
    }



}
