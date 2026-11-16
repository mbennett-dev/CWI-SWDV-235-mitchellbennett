package cwi.edu.Data;

import java.math.BigDecimal;

public class Accounts {
    // 1. Private fields (Encapsulation)
    private int account_id;
    private int user_id;
    private AccountType account_type;
    private BigDecimal balance;

       public enum AccountType {
        CHECKING,
        SAVINGS
    }

    // 2. Default (No-argument) constructor
    public Accounts() {
    }

    // 3. Parameterized constructor
    public Accounts(int account_id, int user_id, AccountType account_type, BigDecimal balance) {
        this.account_id = account_id;
        this.user_id = user_id;
        this.account_type = account_type;
        this.balance = balance;
    }

    // 4. Getter and Setter methods
   public int getAccountId() {
        return account_id;
    }

    public void setAccountId(int accountId) {
        this.account_id = accountId;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int userId) {
        this.user_id = userId;
    }

    public AccountType getAccountType() {
        return account_type;
    }

    public void setAccountType(AccountType accountType) {
        this.account_type = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}