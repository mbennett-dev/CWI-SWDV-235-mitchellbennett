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
    // TransactionController.java
        public synchronized void getAll(Context ctx) {
            // Use ctx to render all transactions html
		    // Pass the transactions model as a parameter
        ctx.render("jte/transactionsPage.jte", Map.of("transactions", transactions));
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
