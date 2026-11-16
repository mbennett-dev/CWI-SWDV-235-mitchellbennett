package cwi.edu.Data;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface AccountsMapper {
    @Select("""
        SELECT account_id, user_id, account_type, balance
        FROM accounts
        WHERE user_id = #{userId}
        ORDER BY account_id
        LIMIT 1
        """)
    Accounts findByUserId(@Param("userId") int userId);

    @Insert("""
        INSERT INTO accounts (user_id, account_type, balance)
        VALUES (#{userId}, #{accountType}, #{balance})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "accountId", keyColumn = "account_id")
    int insert(Accounts account);
}