package com.cdx.bas.application.transaction;

import com.cdx.bas.application.bank.account.BankAccountEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(schema = "basapp", name = "transactions_bank_accounts")
public class TransactionBankAccountsEntity extends PanacheEntityBase {

    @Id
    @JoinColumn(name = "transaction_id", referencedColumnName = "transaction_id")
    private long transactionId;

    @Id
    @ManyToOne
    @JoinColumn(name = "sender_account_id", referencedColumnName = "account_id")
    private BankAccountEntity senderAccount;

    @Id
    @ManyToOne
    @JoinColumn(name = "receiver_account_id", referencedColumnName = "account_id")
    private BankAccountEntity receiverAccount;

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public BankAccountEntity getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(BankAccountEntity senderAccount) {
        this.senderAccount = senderAccount;
    }

    public BankAccountEntity getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(BankAccountEntity receiverAccount) {
        this.receiverAccount = receiverAccount;
    }
}



