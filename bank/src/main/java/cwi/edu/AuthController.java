package cwi.edu;

import cwi.edu.Data.User;
import io.javalin.http.Context;

public class AuthController {

    public static void authenticate(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        User user = new UsersController().authenticateUser(username, password);

        if (user != null) {
            ctx.cookieStore().set("user-id", Integer.toString(user.getUserId()));
            ctx.cookieStore().set("user-role", user.getRole());
            ctx.redirect("/account");
        } else {
            ctx.cookieStore().clear();
            ctx.redirect("/unauthorized.html");
         }
    }

    public static void logout(Context ctx) {
        ctx.cookieStore().clear();
        ctx.redirect("/");
    }
}