<%-- Java imports --%>
@import java.util.Map 

<%-- Parameters --%>
@param Map<String, String> transactions

<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Transactions</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/css/styles.css">
  </head>
  <body>
    <h1>Transactions</h1>
    <a href="/account">Back to account</a>
    <ul>
		<%-- Loop through the transactions map --%>
		@for(var entry : transactions.entrySet())
			<li>${entry.getKey()}: ${entry.getValue()}</li>
		@endfor
    </ul>
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
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
  </body>
</html>