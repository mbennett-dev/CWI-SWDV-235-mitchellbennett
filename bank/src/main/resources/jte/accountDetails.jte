@import java.util.List
@import cwi.edu.Data.User
@import cwi.edu.Data.Accounts
@import cwi.edu.Data.Transactions

@param User user
@param Accounts account
@param List<Transactions> transactions

@template.jte.accountLayout(
    title = "Account Details",
    account = @`
        <a href="/accounts">Back to Accounts</a>
        <h1>${user.getFirstName()} ${user.getLastName()}</h1>
        <p>Account: ${account.getAccountId()}</p>
        <p>Balance: $${account.getBalance()}</p>

        <form action="/account/${account.getAccountId()}/transactions" method="post">
            <label for="transactionType">Type</label>
            <select id="transactionType" name="transactionType" required>
                <option value="DEPOSIT">Deposit</option>
                <option value="WITHDRAWAL">Withdrawal</option>
            </select>

            <label for="amount">Amount</label>
            <input id="amount" name="amount" type="number" min="0.01" step="0.01" required>

            <label for="description">Description</label>
            <input id="description" name="description" required>

            <button type="submit">Add transaction</button>
        </form>

        <h2>Transactions</h2>
        <table class="table">
            <thead>
                <tr><th>Transaction</th><th>Description</th><th>Amount</th></tr>
            </thead>
            <tbody>
                @for(var transaction : transactions)
                    <tr>
                        <td>${transaction.getTransactionId()}</td>
                        <td>${transaction.getDescription()}</td>
                        <td>$${transaction.getAmount()}</td>
                    </tr>
                @else
                    <tr><td colspan="3">No transactions found.</td></tr>
                @endfor
            </tbody>
        </table>
    `
)