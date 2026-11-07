package cwi.edu;

import java.util.Map;
import java.util.HashMap;
import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import static j2html.TagCreator.*;

public class TransactionsController implements CrudHandler {
    private final Map<String, String> transactions = new HashMap<>();

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
        UserAccount account = new UserAccount("0", "Account Holder", AppRole.CUSTOMER, 0.0, transactions);
        ctx.render("jte/accountPage.jte", Map.of(
            "title", "Account",
            "account", account
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
