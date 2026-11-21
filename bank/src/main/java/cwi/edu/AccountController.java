package cwi.edu;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import cwi.edu.Data.Accounts;
import cwi.edu.Data.AccountsMapper;
import cwi.edu.Data.MyBatisConfig;
import cwi.edu.Data.TransactionsMapper;
import cwi.edu.Data.User;
import cwi.edu.Data.UserMapper;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.UnauthorizedResponse;

public class AccountController {
public static void renderAccounts(Context ctx) {
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

    try (SqlSession session = MyBatisConfig.getSqlSessionFactory().openSession()) {
        User user = session.getMapper(UserMapper.class).getById(userId);
        if (user == null) {
            throw new UnauthorizedResponse();
        }

        var accounts = session.getMapper(AccountsMapper.class)
            .findAllByUserId(userId);

        ctx.render("jte/accountsPage.jte", Map.of(
            "user", user,
            "accounts", accounts
        ));
    }
}

public static void renderAccountDetail(Context ctx) {
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

    int accountId;
    try {
        accountId = Integer.parseInt(ctx.pathParam("id"));
    } catch (NumberFormatException e) {
        throw new BadRequestResponse("Invalid account ID");
    }

    try (SqlSession session = MyBatisConfig.getSqlSessionFactory().openSession()) {
        User user = session.getMapper(UserMapper.class).getById(userId);
        if (user == null) {
            throw new UnauthorizedResponse();
        }

        Accounts account = session.getMapper(AccountsMapper.class)
            .findOwnedAccount(accountId, userId);
        if (account == null) {
            throw new NotFoundResponse("Account not found");
        }

        var transactions = session.getMapper(TransactionsMapper.class)
            .findByAccountId(accountId);

        ctx.render("jte/accountDetails.jte", Map.of(
            "user", user,
            "account", account,
            "transactions", transactions
        ));
    }
}
}