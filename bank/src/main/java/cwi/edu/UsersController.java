package cwi.edu;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.mindrot.jbcrypt.BCrypt;

import cwi.edu.Data.Accounts;
import cwi.edu.Data.AccountsMapper;
import cwi.edu.Data.MyBatisConfig;
import cwi.edu.Data.Transactions;
import cwi.edu.Data.TransactionsMapper;
import cwi.edu.Data.User;
import cwi.edu.Data.UserMapper;
import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;

public class UsersController implements CrudHandler {
    
    @Override
    public void getAll(Context ctx) {
        try (SqlSession session = MyBatisConfig.getSqlSessionFactory().openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            ctx.json(mapper.getAllUsers());
        }
    }

    @Override
    public void getOne(Context ctx, String resourceId) {
        try (SqlSession session = MyBatisConfig.getSqlSessionFactory().openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            ctx.json(mapper.getById(Integer.parseInt(resourceId)));
        }
    }

    @Override
    public void create(Context ctx) {
        User user = ctx.bodyAsClass(User.class);
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            ctx.status(400).json(Map.of("error", "Password is required"));
            return;
        }

        user.setRole(AppRole.CUSTOMER.name());
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

        try (SqlSession session = MyBatisConfig.getSqlSessionFactory().openSession()) {
            try {
                UserMapper userMapper = session.getMapper(UserMapper.class);
                AccountsMapper accountsMapper = session.getMapper(AccountsMapper.class);
                TransactionsMapper transactionsMapper =
                    session.getMapper(TransactionsMapper.class);

                if (userMapper.insert(user) != 1 || user.getUserId() <= 0) {
                    throw new IllegalStateException("User insert failed");
                }

                BigDecimal openingDeposit = new BigDecimal("100.00");
                Accounts account = new Accounts(
                    0,
                    user.getUserId(),
                    Accounts.AccountType.CHECKING,
                    openingDeposit
                );
                if (accountsMapper.insert(account) != 1 || account.getAccountId() <= 0) {
                    throw new IllegalStateException("Account insert failed");
                }

                Transactions transaction = new Transactions(
                    null,
                    account.getAccountId(),
                    Transactions.TransactionType.DEPOSIT,
                    openingDeposit,
                    "Opening deposit",
                    null
                );
                if (transactionsMapper.insert(transaction) != 1) {
                    throw new IllegalStateException("Transaction insert failed");
                }

                session.commit();
                ctx.status(201).json(Map.of(
                    "userId", user.getUserId(),
                    "accountId", account.getAccountId()
                ));
            } catch (RuntimeException e) {
                session.rollback();
                throw e;
            }
        }
    }

    public User authenticateUser(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        try (SqlSession session = MyBatisConfig.getSqlSessionFactory().openSession()) {
            User user = session.getMapper(UserMapper.class).findByUsername(username);

            if (user == null || user.getPassword() == null) {
                return null;
            }

            return BCrypt.checkpw(password, user.getPassword()) ? user : null;
        }
    }

    @Override
    public void update(Context ctx, String resourceId) {
        User user = ctx.bodyAsClass(User.class);
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            ctx.status(400).json(Map.of("error", "Password is required"));
            return;
        }

        user.setUserId(Integer.parseInt(resourceId));
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

        try (SqlSession session = MyBatisConfig.getSqlSessionFactory().openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            if (mapper.update(user) != 1) {
                ctx.status(404).json(Map.of("error", "User not found"));
                return;
            }
            session.commit();
            ctx.json(Map.of("userId", user.getUserId(), "updated", true));
        }
    }

    @Override
    public void delete(Context ctx, String resourceId) {
        try (SqlSession session = MyBatisConfig.getSqlSessionFactory().openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            mapper.delete(Integer.parseInt(resourceId));
            session.commit();
            ctx.json(Map.of("id", resourceId, "deleted", true));
        }
    }
}