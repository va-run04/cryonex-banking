package com.cryonex.account.service;

import com.cryonex.account.dto.request.CreateAccountRequest;
import com.cryonex.account.dto.response.AccountResponse;
import com.cryonex.account.entity.Account;
import com.cryonex.account.entity.AccountType;
import com.cryonex.account.enums.AccountStatus;
import com.cryonex.account.exception.BusinessValidationException;
import com.cryonex.account.exception.ResourceNotFoundException;
import com.cryonex.account.repository.AccountRepository;
import com.cryonex.account.repository.AccountTypeRepository;
import com.cryonex.account.util.IdGeneratorUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final AccountTypeRepository accountTypeRepository;

    private final IdGeneratorUtil idGeneratorUtil;

    public AccountService(AccountRepository accountRepository, AccountTypeRepository accountTypeRepository, IdGeneratorUtil idGeneratorUtil){
        this.accountRepository =accountRepository;
        this.idGeneratorUtil = idGeneratorUtil;
        this.accountTypeRepository = accountTypeRepository;
    }

    public AccountResponse createAccount(CreateAccountRequest request){
        AccountType accountType = accountTypeRepository.findByTypeCode(request.getAccountType()).
                orElseThrow(() -> new ResourceNotFoundException("ACC_TYPE__002", "Invalid account type: " + request.getAccountType()));

        if(request.getInitialDeposit().compareTo(accountType.getMinimumBalance()) < 0){
            throw new BusinessValidationException("ACC_08", "Initial Deposit is below the minimum balance required for this account type");
        }

        String accountId = idGeneratorUtil.generateId("ACC", "Account");
        String accountNumber = idGeneratorUtil.generateId("", "AccountNumber", 400000000000L);
        String ifsc = "CRYX0" + request.getBranchCode();

        Account account = new Account();
        account.setAccountId(accountId);
        account.setCustomerId(request.getCustomerId());
        account.setAccountNumber(accountNumber);
        account.setBranchCode(request.getBranchCode());
        account.setAccountType(request.getAccountType());
        account.setIfscCode(ifsc);
        account.setCurrency(request.getCurrency());
        account.setOpeningBalance(request.getInitialDeposit());
        account.setAvailableBalance(request.getInitialDeposit());
        account.setLedgerBalance(request.getInitialDeposit());
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setOpenedDate(LocalDateTime.now());
        accountRepository.save(account);

        return mapToResponse(account);
    }

    private AccountResponse mapToResponse(Account account){
        AccountResponse response = new AccountResponse();
        response.setAccountId(account.getAccountId());
        response.setAccountNumber(account.getAccountNumber());
        response.setIfsc(account.getIfscCode());
        response.setAccountStatus(account.getAccountStatus().name());
        return response;
    }


}
