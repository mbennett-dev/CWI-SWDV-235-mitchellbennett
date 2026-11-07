@param String title
@param cwi.edu.UserAccount account

@template.jte.accountLayout(
    title = title,
    account = @`
        @template.jte.userAccount(name = account.name(), balance = account.formatBalance())
    `,
    detail = @`
        <a href="/account.html">Back to account</a>
        <a href="/logout">Log Out</a>
        @template.jte.transactionsList(transactions = account.transactions())

        <h2>Create transaction</h2>
        <form action="/transactions" method="post">
            <input name="description" placeholder="Transaction details" required>
            <button type="submit">Create</button>
        </form>

        <h2>Update transaction</h2>
        <form action="/transactions/update" method="post">
            <input name="txId" placeholder="Transaction ID" required>
            <input name="description" placeholder="New details" required>
            <button type="submit">Update</button>
        </form>

        <h2>Delete transaction</h2>
        <form action="/transactions/delete" method="post">
            <input name="txId" placeholder="Transaction ID" required>
            <button type="submit">Delete</button>
        </form>
    `
)