package cwi.edu;

import java.math.BigDecimal;
import java.util.Map;

public record UserAccount(
    String id,
    String name,
    AppRole role,
    BigDecimal balance,
    Map<String, String> transactions
) {   
     public String formatBalance() {
         return new java.text.DecimalFormat("$#,##0.00").format(balance);
     }
}
