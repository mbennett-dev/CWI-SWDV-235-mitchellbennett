@import java.util.List
@import cwi.edu.Data.User
@import cwi.edu.Data.Accounts

@param User user
@param List<Accounts> accounts

@template.jte.accountLayout(
    title = "Accounts",
    account = @`
        <h2>${user.getFirstName()} ${user.getLastName()}</h2>
        <h3>User Accounts</h3>

<table class="table">
    <thead>
        <tr>
            <th>Account</th>
            <th>Balance</th>
            <th></th>
        </tr>
    </thead>
    <tbody>
        @for(var account : accounts)
            <tr>
                <td>${account.getAccountId()}</td>
                <td>$${account.getBalance()}</td>
                <td>
                    <a href="/account/${account.getAccountId()}">Details</a>
                </td>
            </tr>
        @else
            <tr><td colspan="3">No accounts found.</td></tr>
        @endfor
    </tbody>
</table>
    `
)
