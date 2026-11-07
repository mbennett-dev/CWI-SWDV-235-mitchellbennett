package cwi.edu;

import java.util.List;

import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.crud;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinJte;

public class Main {
    public static void main(String[] args) {
        TransactionsController transactionsController = new TransactionsController();
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
            config.routes.post("/account", AuthController::authenticate);
            config.routes.get("/logout", AuthController::logout);
            config.routes.post("/transactions/update", ctx ->
                transactionsController.update(ctx, ctx.formParam("txId")));
            config.routes.post("/transactions/delete", ctx ->
                transactionsController.delete(ctx, ctx.formParam("txId")));

            config.routes.beforeMatched(ctx -> {
	            if (ctx.routeRoles().isEmpty()) {
		            return; // if there are no route roles, we don't need to check anything
	            }

	            String role = ctx.cookieStore().get("user-role"); // get the role from the cookie store
	            if (role == null) {
		            throw new UnauthorizedResponse();
	            }
	            AppRole appRole = AppRole.valueOf(role);

	            if (!ctx.routeRoles().contains(appRole)) {
		            throw new UnauthorizedResponse(); // deny access to the route if the user doesn't have the role
	            }
            });

            config.fileRenderer(new JavalinJte()); // <-- set jte as the renderer

            config.routes.apiBuilder(() -> {
                path("/account", List.of(AppRole.CUSTOMER), () -> {
                    get(AccountController::renderAccount); // requires CUSTOMER role
                });
                path("/transactions", () -> {
                    crud("/{id}", transactionsController);
                });
            });
        });

        app.start(7070);
    }
}