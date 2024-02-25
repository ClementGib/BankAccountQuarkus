package com.cdx.bas.application.bank.account;

import com.cdx.bas.domain.bank.account.*;
import com.cdx.bas.domain.bank.account.validation.BankAccountValidator;
import com.cdx.bas.domain.money.Money;
import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.TransactionException;
import com.cdx.bas.domain.bank.transaction.TransactionServicePort;
import com.cdx.bas.domain.currency.rate.ExchangeRateUtils;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@RequestScoped
public class BankAccountServiceImpl implements BankAccountServicePort {
    
    private static final Logger logger = LoggerFactory.getLogger(BankAccountServiceImpl.class);
    
    @Inject
    BankAccountPersistencePort bankAccountRepository;
    
    @Inject
    BankAccountValidator bankAccountValidator;
    
    @Inject
    TransactionServicePort transactionService;

    @Override
    @Transactional
    public Set<BankAccount> getAll() {
        return bankAccountRepository.getAll();
    }

    @Override
    @Transactional
    public BankAccount findBankAccount(Long bankAccountId){
        return bankAccountRepository.findById(bankAccountId).orElse(null);
    }

    @Override
    @Transactional
    public BankAccount addTransaction(Transaction transaction, BankAccount bankAccount) {
        Optional<Transaction> optionalStoredTransaction = bankAccount.getIssuedTransactions().stream()
                .filter(actualTransaction -> actualTransaction.getId().equals(transaction.getId()))
                .findFirst();

        if (optionalStoredTransaction.isPresent()) {
            Transaction mergedTransaction = transactionService.mergeTransactions(optionalStoredTransaction.get(), transaction);
            bankAccount.getIssuedTransactions()
                    .removeIf(existingTransaction -> existingTransaction.getId().equals(mergedTransaction.getId()));
            bankAccount.addTransaction(mergedTransaction);
        } else {
            bankAccount.addTransaction(transaction);
        }
        return bankAccount;
    }

    @Override
    @Transactional
    public BankAccount updateBankAccount(BankAccount bankAccount) throws BankAccountException {
        logger.debug("update bank account" + bankAccount.getId());
        bankAccountValidator.validateBankAccount(bankAccount);
        return bankAccountRepository.update(bankAccount);
    }

    @Override
    public void transferAmountBetweenAccounts(Transaction transaction, BankAccount emitterBankAccount, BankAccount receiverBankAccount) {
        BigDecimal euroAmount = ExchangeRateUtils.getEuroAmountFrom(transaction.getCurrency(), transaction.getAmount());
        if (euroAmount.signum() < 0) {
            throw new TransactionException("Credit transaction " + transaction.getId() + " should have positive value, actual value: " + euroAmount);
        }
        emitterBankAccount.getBalance().minus(Money.of(euroAmount));
        receiverBankAccount.getBalance().plus(Money.of(euroAmount));
        logger.debug("add amount " + emitterBankAccount.getBalance() + " " + transaction.getCurrency()
                + " to bank account" + receiverBankAccount.getId() + " from bank account " + emitterBankAccount.getId());
    }
}
