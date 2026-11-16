@param String title
@param cwi.edu.UserAccount account

@template.jte.accountLayout(
    title = title,
    account = @`
        <h1>${title}</h1>
    `,
    detail = @`
        <a href="/transactions">Transactions</a>
        <a href="/logout">Log Out</a>
        @template.jte.userAccount(name = account.name(), balance = account.formatBalance())
    `
)