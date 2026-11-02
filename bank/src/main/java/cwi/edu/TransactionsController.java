package cwi.edu;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import static j2html.TagCreator.a;
import static j2html.TagCreator.body;
import static j2html.TagCreator.button;
import static j2html.TagCreator.each;
import static j2html.TagCreator.form;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.h2;
import static j2html.TagCreator.input;
import static j2html.TagCreator.li;
import static j2html.TagCreator.p;
import static j2html.TagCreator.ul;

public class TransactionsController implements CrudHandler {
    private final Map<String, String> transactions = new ConcurrentHashMap<>();

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
        public void getAll(Context ctx) {
            // Use ctx to render all transactions html
		    // Pass the transactions model as a parameter
        ctx.render("jte/transactionsPage.jte", Map.of("transactions", transactions));
    }

    @Override
    public void getOne(Context ctx, String resourceId) {
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
    public void update(Context ctx, String resourceId) {
        if (resourceId == null || !transactions.containsKey(resourceId)) {
            ctx.status(404).result("Transaction not found");
            return;
        }

        String description = ctx.formParam("description");
        transactions.put(resourceId, description != null ? description : ctx.body());
        renderAllTransactions(ctx);
    }

    @Override
    public void delete(Context ctx, String resourceId) {
        if (resourceId == null || transactions.remove(resourceId) == null) {
            ctx.status(404).result("Transaction not found");
            return;
        }

        renderAllTransactions(ctx);
    }

    private void renderAllTransactions(Context ctx) {
        ctx.html(
            body(
                h1("Transactions"),
                ul(
                    each(transactions.entrySet(), entry -> li(
                        a("Transaction " + entry.getKey() + ": " + entry.getValue())
                            .withHref("/transactions/" + entry.getKey())
                    ))
                ),
                h2("Create"),
                form().withMethod("post").withAction("/transactions").with(
                    input().withType("text").withName("description").withPlaceholder("Transaction details"),
                    button("Create").withType("submit")
                ),
                h2("Update"),
                form().withMethod("post").withAction("/transactions/update").with(
                    input().withType("text").withName("txId").withPlaceholder("Transaction id"),
                    input().withType("text").withName("description").withPlaceholder("New details"),
                    button("Update").withType("submit")
                ),
                h2("Delete"),
                form().withMethod("post").withAction("/transactions/delete").with(
                    input().withType("text").withName("txId").withPlaceholder("Transaction id"),
                    button("Delete").withType("submit")
                )
            ).render()
        );
    }
}
