package chungnt.own.vn.e_commerce.auth_service.controller;

import chungnt.own.vn.e_commerce.auth_service.dto.AuthRequest;
import chungnt.own.vn.e_commerce.auth_service.dto.AuthResponse;
import chungnt.own.vn.e_commerce.auth_service.entity.User;
import chungnt.own.vn.e_commerce.auth_service.service.UserService;
import chungnt.own.vn.e_commerce.auth_service.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User savedUser = userService.register(user);
            return ResponseEntity.ok("Đăng ký thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        String token = jwtUtil.generateToken(authRequest.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}