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