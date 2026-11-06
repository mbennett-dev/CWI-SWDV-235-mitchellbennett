package cwi.edu;

import java.util.Map;

public record UserAccount(
    String name,
    Double balance,
    Map<String, String> transactions
) {   
     public String formatBalance() {
         return new java.text.DecimalFormat("$#,##0.00").format(balance);
     }
}
