package cwi.edu.Data;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface TransactionsMapper {
    @Insert("""
        INSERT INTO transactions (account_id, transaction_type, amount, description)
        VALUES (#{accountId}, #{transactionType}, #{amount}, #{description})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "transactionId", keyColumn = "transaction_id")
    int insert(Transactions transaction);
}