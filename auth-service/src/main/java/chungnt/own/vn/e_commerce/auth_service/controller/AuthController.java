package chungnt.own.vn.e_commerce.auth_service.controller;

import chungnt.own.vn.e_commerce.auth_service.dto.LoginRequest;
import chungnt.own.vn.e_commerce.auth_service.dto.RegisterRequest;
import chungnt.own.vn.e_commerce.auth_service.entity.User;
import chungnt.own.vn.e_commerce.auth_service.repository.UserRepository;
import chungnt.own.vn.e_commerce.auth_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Email already used";
        }

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
        return "Register success";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return jwtUtil.generateToken(user.getEmail());
            }
        }
        return "Invalid credentials";
    }

    @GetMapping("/me")
    public String me(@RequestHeader("Authorization") String header) {
        String token = header.replace("Bearer ", "");
        return "Logged in as: " + jwtUtil.extractEmail(token);
    }
}