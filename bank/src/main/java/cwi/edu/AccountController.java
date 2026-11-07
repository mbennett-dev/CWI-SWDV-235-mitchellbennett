package cwi.edu;

import java.util.Map;

import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;

public class AccountController {
    public static void renderAccount(Context ctx) {
        String id = ctx.cookieStore().get("user-id");
        UserAccount account = AuthController.getUser(id)
            .orElseThrow(() -> new UnauthorizedResponse());

        ctx.render("jte/accountPage.jte", Map.of(
            "title", "Account",
            "account", account
        ));
    }
}
