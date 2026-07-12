package edu.OrbitaMarket.PaymentsService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository repository;

    void topUp(String userId, Long amount) throws AccountNotFoundException, InvalidAmountException {
        if (amount < 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }
        Account account = repository.findById(userId).orElseThrow(
                () -> new AccountNotFoundException("Account not found"));
        account.setBalance(account.getBalance() + amount);
        repository.save(account);
    }

    void addAccount(String userId) throws AccountNotFoundException {
        Account account = repository.findById(userId).orElse(null);
        if (account == null) {
            Account newAccount = new Account();
            newAccount.setX_user_id(userId);
            newAccount.setBalance(0L);
            repository.save(newAccount);
        }
        else {
            throw new AccountNotFoundException("Account is exist");
        }
    }

    Long getBalance(String userId) throws AccountNotFoundException {
        Account account = findAccount(userId);
        return account.getBalance();
    }

    Account findAccount(String userId) throws AccountNotFoundException {
        return repository.findById(userId).orElseThrow(
                () -> new AccountNotFoundException("Account not found"));
    }

    void expense(String userId, Long amount) throws AccountNotFoundException, InsufficientBalanceException {
        Account account = findAccount(userId);
        Long balance = account.getBalance();
        if (balance - amount < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        account.setBalance(balance - amount);
        repository.save(account);

    }

    void deleteAccount(String userId) { // для тестов и будущего функционала
        repository.deleteById(userId);
    }
}
