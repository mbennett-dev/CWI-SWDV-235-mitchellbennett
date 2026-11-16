package cwi.edu;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import cwi.edu.Data.Accounts;
import cwi.edu.Data.AccountsMapper;
import cwi.edu.Data.MyBatisConfig;
import cwi.edu.Data.User;
import cwi.edu.Data.UserMapper;

import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;

public class AccountController {
    public static void renderAccount(Context ctx) {
        String id = ctx.cookieStore().get("user-id");
        if (id == null) {
            throw new UnauthorizedResponse();
        }

        int userId;
        try {
            userId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new UnauthorizedResponse();
        }

        UserAccount account;
        try (SqlSession session = MyBatisConfig.getSqlSessionFactory().openSession()) {
            User user = session.getMapper(UserMapper.class).getById(userId);
            Accounts bankAccount = session.getMapper(AccountsMapper.class).findByUserId(userId);
            if (user == null || bankAccount == null) {
                throw new UnauthorizedResponse();
            }

            account = new UserAccount(
                id,
                user.getFirstName() + " " + user.getLastName(),
                AppRole.valueOf(user.getRole()),
                bankAccount.getBalance(),
                Map.of()
            );
        }

        ctx.render("jte/accountPage.jte", Map.of(
            "title", "Account",
            "account", account
        ));
    }
}
