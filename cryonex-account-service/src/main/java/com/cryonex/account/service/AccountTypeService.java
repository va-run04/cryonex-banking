package com.cryonex.account.service;

import com.cryonex.account.dto.response.AccountTypeResponse;
import com.cryonex.account.entity.AccountType;
import com.cryonex.account.enums.AccountTypeStatus;
import com.cryonex.account.exception.ResourceNotFoundException;
import com.cryonex.account.repository.AccountTypeRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class AccountTypeService {


    private final AccountTypeRepository accountTypeRepository;

    //constructor injection
    public AccountTypeService(AccountTypeRepository accountTypeRepository){
        this.accountTypeRepository = accountTypeRepository;
    }

    //1) Get All Active account types
   public List<AccountTypeResponse> getAllActiveAccountTypes(){
        List<AccountType> activeType = accountTypeRepository.findAll().stream()
                .filter(type -> type.getAccountStatus() == AccountTypeStatus.ACTIVE)
                .sorted(Comparator.comparing(AccountType::getTypeName)).toList();


        if(activeType.isEmpty()){
            throw new ResourceNotFoundException("ACC_TYPE_001", "No Account Types Available");
        }

        return activeType.stream()
                .map(this::mapToResponse).toList();

   }


   private AccountTypeResponse mapToResponse(AccountType accountType){

        AccountTypeResponse response = new AccountTypeResponse();
        response.setTypeId(accountType.getTypeId());
        response.setTypeCode(accountType.getTypeCode());
        response.setTypeName(accountType.getTypeName());
        response.setDescription(accountType.getDescription());
        response.setMinimumBalance(accountType.getMinimumBalance());
        response.setInterestRate(accountType.getInterestRate());
        response.setWithdrawalLimit(accountType.getWithdrawalLimit());
        response.setDailyTransferLimit(accountType.getDailyTransferLimit());
        response.setMaintenanceCharge(accountType.getMaintenanceCharge());
        response.setChequeBookAvailable(accountType.getChequeBookAvailable());
        response.setDebitCardAvailable(accountType.getDebitCardAvailable());
        response.setPassbookAvailable(accountType.getPassbookAvailable());
        response.setOverdraftAllowed(accountType.getOverdraftAllowed());
        response.setAccountStatus(accountType.getAccountStatus().name());
        return response;
   }

}
