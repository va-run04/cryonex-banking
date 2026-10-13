package com.cryonex.account.service;

import com.cryonex.account.dto.request.CreateAccountRequest;
import com.cryonex.account.dto.request.UpdateMinimumBalanceRequest;
import com.cryonex.account.dto.request.UpdateNicknameRequest;
import com.cryonex.account.dto.response.AccountBalanceResponse;
import org.springframework.data.domain.Pageable;
import com.cryonex.account.dto.response.AccountResponse;
import com.cryonex.account.dto.response.AccountSearchResponse;
import com.cryonex.account.dto.response.CustomerAccountResponse;
import com.cryonex.account.entity.Account;
import com.cryonex.account.entity.AccountType;
import com.cryonex.account.enums.AccountStatus;
import com.cryonex.account.exception.BusinessValidationException;
import com.cryonex.account.exception.ResourceNotFoundException;
import com.cryonex.account.repository.AccountRepository;
import com.cryonex.account.repository.AccountTypeRepository;
import com.cryonex.account.util.IdGeneratorUtil;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    // 1) CreateAccount
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


    // 2) Get Account Balance

    public AccountBalanceResponse getAccountBalance(String accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("ACC_404", "Account Not Found"));

        return mapToBalanceResponse(account);
    }

    private AccountBalanceResponse mapToBalanceResponse(Account account){
        AccountBalanceResponse response = new AccountBalanceResponse();
        response.setAccountNumber(account.getAccountNumber());
        response.setAvailableBalance(account.getAvailableBalance());
        response.setLedgerBalance(account.getLedgerBalance());
        return response;
    }

    // 3) Update NickName
    public void updateNickName(String accountId, UpdateNicknameRequest request){
        Account account = accountRepository.findById(accountId).orElseThrow(() ->new ResourceNotFoundException("ACC_404", "Account Not found"));
        account.setNickname(request.getNickname());
        accountRepository.save(account);
    }

    //4) get Customer Accounts
    public List<CustomerAccountResponse> getCustomerAccounts(String customerId){
        List<Account> accounts = accountRepository.findByCustomerId(customerId);

        return accounts.stream().map(this::mapToCustomerAccountResponse).toList();
    }

    private CustomerAccountResponse mapToCustomerAccountResponse(Account account){
        CustomerAccountResponse response = new CustomerAccountResponse();
        response.setAccountNumber(account.getAccountNumber());
        response.setAccountStatus(account.getAccountStatus().name());
        response.setAccountType(account.getAccountType());
        return response;
    }

    // 5) Update minimum balance
    public void updateMinimumBalance(String accountId, UpdateMinimumBalanceRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("ACC_404", "Account not found"));
        account.setMinimumBalance(request.getMinimumBalance());
        accountRepository.save(account);
    }

    // 6) Search Accounts
    public Page<AccountSearchResponse> searchAccounts(String customerId, String accountNumber,
                                                      String status, String branchCode, Pageable pageable) {

        Specification<Account> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (customerId != null) {
                predicates.add(cb.equal(root.get("customerId"), customerId));
            }
            if (accountNumber != null) {
                predicates.add(cb.equal(root.get("accountNumber"), accountNumber));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("accountStatus"), parseStatus(status)));
            }
            if (branchCode != null) {
                predicates.add(cb.equal(root.get("branchCode"), branchCode));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Account> accounts = accountRepository.findAll(spec, pageable);
        return accounts.map(this::mapToSearchResponse);
    }

    private AccountStatus parseStatus(String status) {
        try {
            return AccountStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessValidationException("ACC_09", "Invalid account status: " + status);
        }
    }

    private AccountSearchResponse mapToSearchResponse(Account account) {
        AccountSearchResponse response = new AccountSearchResponse();
        response.setAccountId(account.getAccountId());
        response.setCustomerId(account.getCustomerId());
        response.setAccountNumber(account.getAccountNumber());
        response.setAccountType(account.getAccountType());
        response.setBranchCode(account.getBranchCode());
        response.setAccountStatus(account.getAccountStatus().name());
        return response;
    }






}
