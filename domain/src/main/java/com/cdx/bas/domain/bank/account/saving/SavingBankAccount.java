package com.cdx.bas.domain.bank.account.saving;

import com.cdx.bas.domain.bank.account.BankAccount;
import com.cdx.bas.domain.bank.account.type.AccountType;
import com.cdx.bas.domain.bank.transaction.Transaction;
import com.cdx.bas.domain.bank.transaction.status.TransactionStatus;
import com.cdx.bas.domain.bank.transaction.type.TransactionType;
import com.cdx.bas.domain.money.Amount;
import com.cdx.bas.domain.money.Money;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static com.cdx.bas.domain.bank.account.type.AccountType.*;

/**
 * Saving Account (French Livret A)
 */
public class SavingBankAccount extends BankAccount {

    @Amount(min=1, max=22950, message="balance amount must be between 1 and 22950.")
    public Money getBalance() {
        return super.balance;
    }
    
    public SavingBankAccount() {
        super(SAVING);
    }

    public SavingBankAccount(Long id, Money balance, List<Long> customersId, Set<Transaction> issuedTransactions) {
        super(id, SAVING, balance, customersId, issuedTransactions);
    }
}
