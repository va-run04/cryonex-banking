package com.cryonex.customer.controller;

import com.cryonex.customer.dto.ApiResponse;
import com.cryonex.customer.dto.request.LoginRequestDto;
import com.cryonex.customer.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

// TEMPORARY — mock login for testing only. Replace with a real auth-service later.
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/mock-login")
    public ResponseEntity<ApiResponse> mockLogin(@Valid @RequestBody LoginRequestDto request) {

        String token = jwtUtil.generateToken(request.getUsername(), request.getRole());

        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        data.put("username", request.getUsername());
        data.put("role", request.getRole());

        return ResponseEntity.ok(ApiResponse.success("Mock token generated successfully.", data));
    }

}