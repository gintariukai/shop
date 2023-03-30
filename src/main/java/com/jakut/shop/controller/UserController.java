package com.jakut.shop.controller;

import com.jakut.shop.AppUtils;
import com.jakut.shop.jwt.JwtTokenProvider;
import com.jakut.shop.model.*;
import com.jakut.shop.service.ProductService;
import com.jakut.shop.service.TransactionService;
import com.jakut.shop.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    private final UserService userService;

    private final ProductService productService;

    private final TransactionService transactionService;

    @PostMapping("/api/user/registration")
    public ResponseEntity<?> register(@RequestBody User user){
        if(userService.findByUsername(user.getUsername())!=null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        //default role.
        user.setRole(Role.USER);
        return new ResponseEntity<>(AppUtils.toUserLoginResponse(userService.saveUser(user)), HttpStatus.CREATED);
    }

    @PostMapping("/api/user/login")
    public UserLoginResponseDto getUser(@RequestBody UserLoginRequestDto loginRequest) {

        User user = authenticate(loginRequest);

        UserLoginResponseDto response = new UserLoginResponseDto();
        response.setId(user.getId());
        response.setToken(generateJwt(user));
        response.setName(user.getName());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole());

        return response;
    }

    private String generateJwt(User user) {
        return tokenProvider.createToken(user);
    }

    private User authenticate(UserLoginRequestDto loginRequest) {
        AbstractAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        return (User) authenticationManager.authenticate(token)
                .getPrincipal();
    }

    @PostMapping("/api/user/purchase")
    public ResponseEntity<?> purchaseProduct(@RequestBody Transaction transaction){
        transaction.setPurchaseDate(LocalDateTime.now());
        transactionService.saveTransaction(transaction);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @GetMapping("/api/user/products")
    public ResponseEntity<?> getAllProducts(){
        return new ResponseEntity<>(productService.findAllProducts(), HttpStatus.OK);
    }
}