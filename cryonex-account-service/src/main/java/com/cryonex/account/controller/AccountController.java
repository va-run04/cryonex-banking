package com.cryonex.account.controller;

import com.cryonex.account.dto.ApiResponse;
import com.cryonex.account.dto.request.CreateAccountRequest;
import com.cryonex.account.dto.request.UpdateMinimumBalanceRequest;
import com.cryonex.account.dto.request.UpdateNicknameRequest;
import com.cryonex.account.dto.response.AccountBalanceResponse;
import com.cryonex.account.dto.response.AccountResponse;
import com.cryonex.account.dto.response.CustomerAccountResponse;
import com.cryonex.account.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    //1) Create Account
    @PostMapping
    @PreAuthorize("hasRole('BANK_EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createAccount(@Valid @RequestBody CreateAccountRequest request){
        AccountResponse response = accountService.createAccount(request);
        return new ResponseEntity<>(ApiResponse.success("Account Created Successfully", response), HttpStatus.CREATED);
    }

    // 2) Get Account Balance
    @GetMapping("/{accountId}/balance")
    @PreAuthorize("hasAnyRole('BANK_EMPLOYEE', 'ADMIN')")
    public ResponseEntity<ApiResponse> getAccountBalance(@PathVariable String accountId){
        AccountBalanceResponse response = accountService.getAccountBalance(accountId);
        return ResponseEntity.ok(ApiResponse.success("Balance fetched successfully", response));
    }

    //3)Update NickName
    @PutMapping("/{accountId}/nickname")
    @PreAuthorize("hasAnyRole('BANK_EMPLOYEE', 'ADMIN')")
    public ResponseEntity<ApiResponse> updateNickName(@PathVariable String accountId, @Valid @RequestBody UpdateNicknameRequest request){
        accountService.updateNickName(accountId, request);
        return ResponseEntity.ok(ApiResponse.success("Nick name updated successfully",null));
    }

    //4) Get Customer Accounts
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('BANK_EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getCustomerAccounts(@PathVariable String customerId) {
        List<CustomerAccountResponse> accounts = accountService.getCustomerAccounts(customerId);
        return ResponseEntity.ok(ApiResponse.success("Customer accounts fetched successfully.", accounts));
    }

    // 5) Update minimum balance
    @PutMapping("/{accountId}/minimum-balance")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateMinimumBalance(@PathVariable String accountId, @Valid @RequestBody UpdateMinimumBalanceRequest request) {
        accountService.updateMinimumBalance(accountId, request);
        return ResponseEntity.ok(ApiResponse.success("Minimum balance updated successfully.", null));
    }


}
