package cwi.edu.Data;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TransactionsMapper {
    @Select("""
        SELECT transaction_id, account_id, transaction_type, amount, description, created_at
        FROM transactions
        WHERE account_id = #{accountId}
        ORDER BY transaction_id
        """)
    List<Transactions> findByAccountId(@Param("accountId") int accountId);

    @Insert("""
        INSERT INTO transactions (account_id, transaction_type, amount, description)
        VALUES (#{accountId}, #{transactionType}, #{amount}, #{description})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "transactionId", keyColumn = "transaction_id")
    int insert(Transactions transaction);
}