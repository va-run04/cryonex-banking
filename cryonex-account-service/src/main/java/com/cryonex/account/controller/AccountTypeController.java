package com.cryonex.account.controller;

import com.cryonex.account.dto.ApiResponse;
import com.cryonex.account.dto.response.AccountTypeResponse;
import com.cryonex.account.service.AccountTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account-types")
public class AccountTypeController {

    private final AccountTypeService accountTypeService;

    public AccountTypeController(AccountTypeService accountTypeService){
        this.accountTypeService = accountTypeService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('BANK_EMPLOYEE', 'ADMIN')")
    public ResponseEntity<ApiResponse> getAllActiveAccountTypes(){
        List<AccountTypeResponse> accountTypes = accountTypeService.getAllActiveAccountTypes();
        return ResponseEntity.ok(ApiResponse.success("Account types fetched successfully.", accountTypes));
    }
}
