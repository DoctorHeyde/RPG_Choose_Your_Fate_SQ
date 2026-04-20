package dk.ek.gruppe2.chooseyourfate.controller;

import org.springframework.web.bind.annotation.*;

import dk.ek.gruppe2.chooseyourfate.dto.CreateAccountRequestDTO;
import dk.ek.gruppe2.chooseyourfate.dto.LoginDTO;
import dk.ek.gruppe2.chooseyourfate.repository.mysql.AccountRepository;
import dk.ek.gruppe2.chooseyourfate.security.JwtUtil;
import dk.ek.gruppe2.chooseyourfate.service.mysql.AccountService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    private final AccountService accountService;

    public AuthenticationController(AuthenticationManager authManager,
                          JwtUtil jwtUtil,
                          AccountRepository repo,
                          
                        AccountService accountService) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.accountService = accountService;
    }
    
    @PostMapping("/register")
    public String register(@RequestBody CreateAccountRequestDTO acc) {
        accountService.createAccount(acc);

        return "Registered";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO request) {
        // SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        // System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));

        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.username,
                request.password
            )
        );

        UserDetails user = (UserDetails) auth.getPrincipal();
        return jwtUtil.generateToken(user);
    }
}