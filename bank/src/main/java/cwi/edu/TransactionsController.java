package cwi.edu;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import cwi.edu.Data.AccountsMapper;
import cwi.edu.Data.MyBatisConfig;
import cwi.edu.Data.Transactions;
import cwi.edu.Data.TransactionsMapper;
import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.UnauthorizedResponse;
import static j2html.TagCreator.*;

public class TransactionsController implements CrudHandler {
    private final Map<String, String> transactions = new HashMap<>();

    public void createForAccount(Context ctx) {
        int accountId;
        try {
            accountId = Integer.parseInt(ctx.pathParam("id"));
        } catch (NumberFormatException e) {
            throw new BadRequestResponse("Invalid account ID");
        }

        String userIdCookie = ctx.cookieStore().get("user-id");
        if (userIdCookie == null) {
            throw new UnauthorizedResponse();
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdCookie);
        } catch (NumberFormatException e) {
            throw new UnauthorizedResponse();
        }

        Transactions.TransactionType transactionType;
        try {
            transactionType = Transactions.TransactionType.valueOf(
                ctx.formParam("transactionType"));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new BadRequestResponse("Invalid transaction type");
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(ctx.formParam("amount"));
        } catch (NumberFormatException | NullPointerException e) {
            throw new BadRequestResponse("Invalid transaction amount");
        }
        if (amount.signum() <= 0) {
            throw new BadRequestResponse("Transaction amount must be positive");
        }

        String description = ctx.formParam("description");
        if (description == null || description.isBlank()) {
            throw new BadRequestResponse("Transaction description is required");
        }

        try (SqlSession session = MyBatisConfig.getSqlSessionFactory().openSession()) {
            if (session.getMapper(AccountsMapper.class)
                    .findOwnedAccount(accountId, userId) == null) {
                throw new NotFoundResponse("Account not found");
            }

            Transactions transaction = new Transactions();
            transaction.setAccountId(accountId);
            transaction.setTransactionType(transactionType);
            transaction.setAmount(amount);
            transaction.setDescription(description);

            if (session.getMapper(TransactionsMapper.class).insert(transaction) != 1) {
                throw new IllegalStateException("Transaction insert failed");
            }

            session.commit();
        }

        ctx.redirect("/account/" + accountId);
    }

    @Override
    public synchronized void create(Context ctx) {
        String description = ctx.formParam("description");
        if (description == null) {
            description = ctx.body();
        }

        String id = String.valueOf(transactions.size() + 1);
        transactions.put(id, description);
        renderAllTransactions(ctx);
    }

    @Override
    public synchronized void getAll(Context ctx) {
        ctx.render("jte/transactionsPage.jte", Map.of(
            "transactions", transactions
        ));
    }

    @Override
    public synchronized void getOne(Context ctx, String resourceId) {
        String description = transactions.get(resourceId);
        if (description == null) {
            ctx.status(404).result("Transaction not found");
            return;
        }

        ctx.html(
            body(
                h1("Transaction " + resourceId),
                p(description),
                a("Back to all transactions").withHref("/transactions")
            ).render()
        );
    }

    @Override
    public synchronized void update(Context ctx, String resourceId) {
        if (resourceId == null || !transactions.containsKey(resourceId)) {
            ctx.status(404).result("Transaction not found");
            return;
        }

        String description = ctx.formParam("description");
        transactions.put(resourceId, description != null ? description : ctx.body());
        renderAllTransactions(ctx);
    }

    @Override
    public synchronized void delete(Context ctx, String resourceId) {
        if (resourceId == null || transactions.remove(resourceId) == null) {
            ctx.status(404).result("Transaction not found");
            return;
        }

        renderAllTransactions(ctx);
    }

    private void renderAllTransactions(Context ctx) {
        ctx.redirect("/transactions");
    }
}
