package cwi.edu;

public record UserAccount(
    String username,
    String password,
    double balance
) {   
    // public String formattedBalance() {
    //     return new java.text.DecimalFormat("$#,###.00").format(balance);
    // }
}
