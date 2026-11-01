package cwi.edu;

import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.crud;
import static io.javalin.apibuilder.ApiBuilder.path;
import io.javalin.http.staticfiles.Location;

public class Main {
    public static void main(String[] args) {
        TransactionsController transactionsController = new TransactionsController();
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/cwi/edu/resources/public", Location.CLASSPATH);
            config.routes.post("/account", ctx -> ctx.redirect("/account.html"));
            config.routes.get("/logout", ctx -> ctx.redirect("/"));
            config.routes.post("/transactions/update", ctx ->
                transactionsController.update(ctx, ctx.formParam("txId")));
            config.routes.post("/transactions/delete", ctx ->
                transactionsController.delete(ctx, ctx.formParam("txId")));

            config.routes.apiBuilder(() -> {
                path("/transactions", () -> {
                    crud("/{id}", transactionsController);
                });
            });
        });

        app.start(7070);
    }
}