package com.example.bank.Service;

import com.example.bank.dto.AdminAccountViewDto;
import com.example.bank.dto.CreateAccountDto;
import com.example.bank.mapper.AccountMapper;
import com.example.bank.model.*;
import com.example.bank.repository.CreditUsersRepository;
import com.example.bank.repository.DebitUsersRepository;
import com.example.bank.repository.SavingsUserRpository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class AccountService {

    private final DebitUsersRepository debitUsersRepository;
    private final CreditUsersRepository creditUsersRepository;
    private final SavingsUserRpository savingsUsersRepository;
    private final TimeService timeService;
    private final AccountMapper accountMapper;
    private  ScoreTypeModel scoreTypeModel;

    public AccountService(DebitUsersRepository debitUsersRepository,
                          CreditUsersRepository creditUsersRepository,
                          SavingsUserRpository savingsUsersRepository, TimeService timeService, AccountMapper accountMapper) {

        this.debitUsersRepository = debitUsersRepository;
        this.creditUsersRepository = creditUsersRepository;
        this.savingsUsersRepository = savingsUsersRepository;
        this.timeService = timeService;
        this.accountMapper = accountMapper;
    }

    //-----------------------------create score-----------------------------

    public DebitUsersModel createDebitUsersScore(UsersModel user) {

        if (debitUsersRepository.findByUserId(user.getId()).size() >= 3)
            throw new RuntimeException("Достигнут лимит кредитных счетов (максимум 3)");

        DebitUsersModel debitUsersModel = new DebitUsersModel();
        debitUsersModel.setUser(user);
        debitUsersModel.setBalance(BigDecimal.ZERO);
        debitUsersModel.setLastmaintancedays(timeService.getLocalDateTime());
        return debitUsersRepository.save(debitUsersModel);
    }

    public CreditUsersModel createCreditUsersScore(UsersModel user, BigDecimal creditLimit) {

        if (creditUsersRepository.findByUserId(user.getId()).size() >= 2)
            throw new RuntimeException("Достигнут лимит кредитных счетов (максимум 2)");

        CreditUsersModel creditUsersModel = new CreditUsersModel();
        creditUsersModel.setUser(user);
        creditUsersModel.setBalance(BigDecimal.valueOf(100000));
        creditUsersModel.setCreditLimit(creditLimit);
        creditUsersModel.setLastmaintancedays(timeService.getLocalDateTime());
        return creditUsersRepository.save(creditUsersModel);
    }

    public SavingsUsersModel createSavingsUsersScore(UsersModel user) {
        if (savingsUsersRepository.findByUserId(user.getId()).size() >= 1)  {
            throw new RuntimeException("Достигнут лимит кредитных счетов (максимум 1)");
        }
        SavingsUsersModel savingsUsersModel = new SavingsUsersModel();
        savingsUsersModel.setUser(user);
        savingsUsersModel.setBalance(BigDecimal.ZERO);
        savingsUsersModel.setInterestRate(0.01);
        savingsUsersModel.setLastmaintancedays(timeService.getLocalDateTime());
        savingsUsersModel.setLastchangebalance(timeService.getLocalDateTime());
        return savingsUsersRepository.save(savingsUsersModel);
    }

    //-----------------------------------------------------------------------

    //-----------------------------operations score-----------------------------

    public DebitUsersModel depositToDebit(int accountId, int userId, BigDecimal amount) {
        DebitUsersModel debitUsersModel = debitUsersRepository.findByIdAndUserId(accountId, userId);
        if (debitUsersModel == null)
            throw new RuntimeException("Счет не найден");

        debitUsersModel.setBalance(debitUsersModel.getBalance().add(amount));
        return debitUsersRepository.save(debitUsersModel);
    }

    public DebitUsersModel withdrawFromDebit(int accountId, int userId, BigDecimal amount) {
        DebitUsersModel debitUsersModel = debitUsersRepository.findByIdAndUserId(accountId, userId);
        if (debitUsersModel == null)
            throw new RuntimeException("Счет не найден");

        if (debitUsersModel.getBalance().compareTo(amount) < 0)
            throw new RuntimeException("Недостаточно средств на счете");

        debitUsersModel.setBalance(debitUsersModel.getBalance().subtract(amount));
        return debitUsersRepository.save(debitUsersModel);
    }

    public CreditUsersModel depositToCredit(int accountId, int userId, BigDecimal amount) {
        CreditUsersModel creditUsersModel = creditUsersRepository.findByIdAndUserId(accountId, userId);
        BigDecimal balance = BigDecimal.valueOf(100000);

        if (creditUsersModel == null)
            throw new RuntimeException("Счет не найден");
        if (creditUsersModel.getBalance().add(amount).compareTo(balance) > 0)
            throw new RuntimeException("Больше" + balance + " пополнить нельзя");

        creditUsersModel.setBalance(creditUsersModel.getBalance().add(amount));
        return creditUsersRepository.save(creditUsersModel);
    }

    public CreditUsersModel withdrawFromCredit(int accountId, int userId, BigDecimal amount) {
        CreditUsersModel creditUsersModel = creditUsersRepository.findByIdAndUserId(accountId, userId);
        if (creditUsersModel == null)
            throw new RuntimeException("Счет не найден");

        BigDecimal limit = creditUsersModel.getCreditLimit();
        BigDecimal balance = creditUsersModel.getBalance();

        if (balance.subtract(amount).compareTo(limit) < 0)
            throw new RuntimeException("Превышен лимит");


        creditUsersModel.setBalance(creditUsersModel.getBalance().subtract(amount));
        return creditUsersRepository.save(creditUsersModel);
    }

    public SavingsUsersModel depositToSavings(int accountId, int userId, BigDecimal amount) {
        SavingsUsersModel savingsUsersModel = savingsUsersRepository.findByIdAndUserId(accountId, userId);
        LocalDateTime today = timeService.getLocalDateTime();

        if (savingsUsersModel == null)
            throw new RuntimeException("Такой счёт не найден");

        savingsUsersModel.setBalance(savingsUsersModel.getBalance().add(amount));
        savingsUsersModel.setLastchangebalance(today);
        return savingsUsersRepository.save(savingsUsersModel);
    }

    public SavingsUsersModel withdrawFromSavings(int accountId, int userId, BigDecimal amount) {
        SavingsUsersModel savingsUsersModel = savingsUsersRepository.findByIdAndUserId(accountId, userId);
        LocalDateTime today = timeService.getLocalDateTime();

        if (savingsUsersModel == null)
            throw new RuntimeException("Счет не найден");
        if (savingsUsersModel.getBalance().compareTo(amount) < 0)
            throw new RuntimeException("Сумма снятия не может быть больше доступной суммы на счету");
        savingsUsersModel.setBalance(savingsUsersModel.getBalance().subtract(amount));
        savingsUsersModel.setLastchangebalance(today);

        return savingsUsersRepository.save(savingsUsersModel);
    }

    public void transferFromAccount(
        String fromType,
        int fromId, //userId
        String onType,
        int onId,
        int userId,
        BigDecimal amount) {

        LocalDateTime today = timeService.getLocalDateTime();

        if (fromType.equals(onType) && fromId == onId)
            throw new RuntimeException("Счёт перевода должен отличаться от счёта снятия");
        if (fromType.equals(scoreTypeModel.CREDIT.name()) && onType.equals(scoreTypeModel.CREDIT.name()))
            throw new RuntimeException("Переводы между кредитными счетами запрещены");

        //DEBIT-DEBIT
       if (fromType.equals(scoreTypeModel.DEBIT.name()) && onType.equals(scoreTypeModel.DEBIT.name()) && onId != userId) {
           DebitUsersModel fromDebitUsersModel = debitUsersRepository.findByIdAndUserId(fromId, userId);
           DebitUsersModel onDebitUsersModel = debitUsersRepository.findByIdAndUserId(onId, userId);

           if (fromDebitUsersModel == null) throw new RuntimeException("Исходный счет не найден");
           if (onDebitUsersModel == null) throw new RuntimeException("Целевой счет не найден");

           if (fromDebitUsersModel.getBalance().compareTo(amount) < 0)
               throw new RuntimeException("Недостаточно средств на счете снятия");

           fromDebitUsersModel.setBalance(fromDebitUsersModel.getBalance().subtract(amount));
           debitUsersRepository.save(fromDebitUsersModel);
           onDebitUsersModel.setBalance(onDebitUsersModel.getBalance().add(amount));
           debitUsersRepository.save(onDebitUsersModel);
       } else if (fromType.equals(scoreTypeModel.DEBIT.name()) && onType.equals(scoreTypeModel.CREDIT.name())) { //DEBIT-CREDIT
           DebitUsersModel fromDebitUsersModel = debitUsersRepository.findByIdAndUserId(fromId, userId);
           CreditUsersModel oncreditUsersModel = creditUsersRepository.findByIdAndUserId(onId, userId);

           if (fromDebitUsersModel == null) throw new RuntimeException("Исходный счет не найден");
           if (oncreditUsersModel == null) throw new RuntimeException("Целевой счет не найден");

           if (fromDebitUsersModel.getBalance().compareTo(amount) < 0)
               throw new RuntimeException("Недостаточно средств на счете снятия");

           fromDebitUsersModel.setBalance(fromDebitUsersModel.getBalance().subtract(amount));
           debitUsersRepository.save(fromDebitUsersModel);
           oncreditUsersModel.setBalance(oncreditUsersModel.getBalance().add(amount));
           creditUsersRepository.save(oncreditUsersModel);
       } else if (fromType.equals(scoreTypeModel.CREDIT.name()) && onType.equals(scoreTypeModel.DEBIT.name())) { //CREDIT-DEBIT
           CreditUsersModel fromCreditUsersModel = creditUsersRepository.findByIdAndUserId(fromId, userId);
           DebitUsersModel onDebitUsersModel = debitUsersRepository.findByIdAndUserId(onId, userId);

           if (fromCreditUsersModel == null) throw new RuntimeException("Исходный счет не найден");
           if (onDebitUsersModel == null) throw new RuntimeException("Целевой счет не найден");

           if (fromCreditUsersModel.getBalance().compareTo(amount) < 0)
               throw new RuntimeException("Недостаточно средств на счете снятия");

           fromCreditUsersModel.setBalance(fromCreditUsersModel.getBalance().subtract(amount));
           creditUsersRepository.save(fromCreditUsersModel);
           onDebitUsersModel.setBalance(onDebitUsersModel.getBalance().add(amount));
           debitUsersRepository.save(onDebitUsersModel);
       } else if (fromType.equals(scoreTypeModel.CREDIT.name()) && onType.equals(scoreTypeModel.SAVINGS.name())) { //CREDIT-SAVINGS
           CreditUsersModel fromCreditUsersModel = creditUsersRepository.findByIdAndUserId(fromId, userId);
           SavingsUsersModel onSavingsUsersModel = savingsUsersRepository.findByIdAndUserId(onId, userId);

           if (fromCreditUsersModel == null) throw new RuntimeException("Исходный счет не найден");
           if (onSavingsUsersModel == null) throw new RuntimeException("Целевой счет не найден");

           if (fromCreditUsersModel.getBalance().compareTo(amount) < 0)
               throw new RuntimeException("Недостаточно средств на счете снятия");

           fromCreditUsersModel.setBalance(fromCreditUsersModel.getBalance().subtract(amount));
           creditUsersRepository.save(fromCreditUsersModel);
           onSavingsUsersModel.setBalance(onSavingsUsersModel.getBalance().add(amount));
           onSavingsUsersModel.setLastchangebalance(today);
           savingsUsersRepository.save(onSavingsUsersModel);
       } else if (fromType.equals(scoreTypeModel.SAVINGS.name()) && onType.equals(scoreTypeModel.CREDIT.name())) { //SAVING-CREDIT
           SavingsUsersModel fromSavingsUsersModel = savingsUsersRepository.findByIdAndUserId(fromId, userId);
           CreditUsersModel onCreditUsersModel = creditUsersRepository.findByIdAndUserId(onId, userId);

           if (onCreditUsersModel == null) throw new RuntimeException("Исходный счет не найден");
           if (fromSavingsUsersModel == null) throw new RuntimeException("Целевой счет не найден");

           if (fromSavingsUsersModel.getBalance().compareTo(amount) < 0)
               throw new RuntimeException("Недостаточно средств на счете снятия");

           fromSavingsUsersModel.setBalance(fromSavingsUsersModel.getBalance().subtract(amount));
           fromSavingsUsersModel.setLastchangebalance(today);
           savingsUsersRepository.save(fromSavingsUsersModel);
           onCreditUsersModel.setBalance(onCreditUsersModel.getBalance().add(amount));
           creditUsersRepository.save(onCreditUsersModel);
       } else if (fromType.equals(scoreTypeModel.SAVINGS.name()) && onType.equals(scoreTypeModel.DEBIT.name())) { //SAVINGS-DEBIT
           SavingsUsersModel fromSavingsUsersModel = savingsUsersRepository.findByIdAndUserId(fromId, userId);
           DebitUsersModel onDebitUsersModel = debitUsersRepository.findByIdAndUserId(onId, userId);

           if (onDebitUsersModel == null) throw new RuntimeException("Исходный счет не найден");
           if (fromSavingsUsersModel == null) throw new RuntimeException("Целевой счет не найден");

           if (fromSavingsUsersModel.getBalance().compareTo(amount) < 0)
               throw new RuntimeException("Недостаточно средств на счете снятия");

           fromSavingsUsersModel.setBalance(fromSavingsUsersModel.getBalance().subtract(amount));
           fromSavingsUsersModel.setLastchangebalance(today);
           savingsUsersRepository.save(fromSavingsUsersModel);
           onDebitUsersModel.setBalance(onDebitUsersModel.getBalance().add(amount));
           debitUsersRepository.save(onDebitUsersModel);
       } else if (fromType.equals(scoreTypeModel.DEBIT.name()) && onType.equals(scoreTypeModel.SAVINGS.name())) { //DEBIT-SAVING
           DebitUsersModel fromDebitUsersModel = debitUsersRepository.findByIdAndUserId(fromId, userId);
           SavingsUsersModel onSavingsUsersModel = savingsUsersRepository.findByIdAndUserId(onId, userId);

           if (fromDebitUsersModel == null) throw new RuntimeException("Исходный счет не найден");
           if (onSavingsUsersModel == null) throw new RuntimeException("Целевой счет не найден");

           if (fromDebitUsersModel.getBalance().compareTo(amount) < 0)
               throw new RuntimeException("Недостаточно средств на счете снятия");

           fromDebitUsersModel.setBalance(fromDebitUsersModel.getBalance().subtract(amount));
           debitUsersRepository.save(fromDebitUsersModel);
           onSavingsUsersModel.setBalance(onSavingsUsersModel.getBalance().add(amount));
           onSavingsUsersModel.setLastchangebalance(today);
           savingsUsersRepository.save(onSavingsUsersModel);
       }

}

    //------------------------------------------------------------------------

    //-----------------------------misc---------------------------------------

    @Transactional
    public void processMaintenanceFees (){
        LocalDateTime today = timeService.getLocalDateTime();

        for (DebitUsersModel debitUsersModel : debitUsersRepository.findAll()) {
            if (needsMaintenance(debitUsersModel.getLastmaintancedays(),today)
                    && debitUsersModel.getBalance().compareTo(BigDecimal.valueOf(100)) >= 0)
            {
                debitUsersModel.setBalance(debitUsersModel.getBalance().subtract(BigDecimal.valueOf(100)));
                debitUsersModel.setLastmaintancedays(today);
                debitUsersRepository.save(debitUsersModel);
            }
        }

        for (CreditUsersModel creditUsersModel : creditUsersRepository.findAll()) {
            if (creditUsersModel.getBalance().signum() < 0 &&
                    needsMaintenance(creditUsersModel.getLastmaintancedays(),today))
            {
                BigDecimal balance = creditUsersModel.getBalance().abs();
                balance = balance.multiply(new BigDecimal("0.015"));
                creditUsersModel.setBalance(creditUsersModel.getBalance().subtract(balance));
                creditUsersModel.setLastmaintancedays(today);
                creditUsersRepository.save(creditUsersModel);
            }
        }

        for (SavingsUsersModel savingsUsersModel : savingsUsersRepository.findAll()) {
            if (savingsUsersModel.getBalance().compareTo(BigDecimal.valueOf(0)) == 0) continue;
            if (!needsMaintenance(savingsUsersModel.getLastmaintancedays(),today))continue;

            BigDecimal interest = savingsUsersModel.getBalance().multiply(BigDecimal.valueOf(savingsUsersModel.getInterestRate()));
            savingsUsersModel.setBalance(savingsUsersModel.getBalance().add(interest));
            savingsUsersRepository.save(savingsUsersModel);
        }
    }

    private boolean needsMaintenance(LocalDateTime lastDate, LocalDateTime today) {
        return ChronoUnit.DAYS.between(lastDate, today) >= 3;
    }

    public void pepositorySwitch(int page, String type, Model model)
    {
        Pageable pageable = PageRequest.of(page,10);
        Page<AdminAccountViewDto> accountPage;
        String accountType = type.toUpperCase();

        switch (accountType){
            case "CREDIT":
                Page<CreditUsersModel> creditPage =creditUsersRepository.findAll(pageable);
                accountPage = creditPage.map(accountMapper::toAdminViewDto);
                break;
            case "SAVINGS":
                Page<SavingsUsersModel> savingsPage = savingsUsersRepository.findAll(pageable);
                accountPage = savingsPage.map(accountMapper::toAdminViewDto);
                break;
            default:
                Page<DebitUsersModel> debitPage = debitUsersRepository.findAll(pageable);
                accountPage = debitPage.map(accountMapper::toAdminViewDto);
                accountType = "DEBIT";
        }

        model.addAttribute("accountsPage", accountPage);
        model.addAttribute("currentType", accountType.toLowerCase());
        model.addAttribute("currentPage", page);
        model.addAttribute("bankTime", timeService.getLocalDateTime());
    }

    public boolean checkCreditLimit(CreateAccountDto dto)
    {
        return dto.getCreditLimit() == null || dto.getCreditLimit().compareTo(BigDecimal.ZERO) > 0
                || dto.getCreditLimit().compareTo(new BigDecimal("-100000")) < 0;
    }
}
