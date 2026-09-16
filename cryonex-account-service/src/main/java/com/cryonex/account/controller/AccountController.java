package com.cryonex.account.controller;

import com.cryonex.account.dto.ApiResponse;
import com.cryonex.account.dto.request.CreateAccountRequest;
import com.cryonex.account.dto.response.AccountResponse;
import com.cryonex.account.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }
    @PostMapping
    @PreAuthorize("hasRole('BANK_EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createAccount(@Valid @RequestBody CreateAccountRequest request){
        AccountResponse response = accountService.createAccount(request);
        return new ResponseEntity<>(ApiResponse.success("Account Created Successfully", response), HttpStatus.CREATED);
    }
}
