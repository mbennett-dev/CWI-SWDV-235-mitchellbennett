package cwi.edu.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
    

public class Transactions {
    // 1. Private fields (Encapsulation)
    private BigInteger transaction_id;
    private int account_id;
    private TransactionType transaction_type;
    private BigDecimal amount;
    private String description;
    private LocalDateTime created_at;

    public enum TransactionType {
        DEPOSIT,
        WITHDRAWAL,
        TRANSFER_IN,
        TRANSFER_OUT
    }


    // 2. Default (No-argument) constructor
    public Transactions() {
    }

    // 3. Parameterized constructor
    public Transactions(BigInteger transaction_id, int account_id, TransactionType transaction_type,
        BigDecimal amount, String description, LocalDateTime created_at) {
        this.transaction_id = transaction_id;
        this.account_id = account_id;
        this.transaction_type = transaction_type;
        this.amount = amount;
        this.description = description;
        this.created_at = created_at;
    }

    // 4. Getter and Setter methods
 public BigInteger getTransactionId() {
        return transaction_id;
    }

    public void setTransactionId(BigInteger transactionId) {
        this.transaction_id = transactionId;
    }

    public int getAccountId() {
        return account_id;
    }

    public void setAccountId(int accountId) {
        this.account_id = accountId;
    }

    public TransactionType getTransactionType() {
        return transaction_type;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transaction_type = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.created_at = createdAt;
    }
}