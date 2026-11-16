package cwi.edu;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import gg.jte.resolve.DirectoryCodeResolver;
import java.nio.file.Path;
import java.util.Map;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AccountPageTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void rendersAccountWithoutTransactionActivity() throws Exception {
        String html = render(Map.of("1", "Deposit"));

        assertTrue(html.contains("<title>Account test</title>"));
        assertTrue(html.contains("Test User"));
        assertTrue(html.contains("$1,234.50"));
        assertFalse(html.contains("$$"));
        assertTrue(html.contains("href=\"/logout\""));
        assertTrue(html.contains("href=\"/transactions\""));
        assertFalse(html.contains("Quick Actions"));
        assertTrue(html.indexOf("<h1>Account test</h1>") < html.indexOf("href=\"/transactions\""));
        assertTrue(html.indexOf("href=\"/logout\"") < html.indexOf("Test User"));
        assertFalse(html.contains("Recent Activity"));
        assertFalse(html.contains("Deposit"));
        assertFalse(html.contains("action=\"/transactions"));
    }

    @Test
    public void rendersTransactionsWithForms() throws Exception {
        String html = renderTransactions(Map.of("1", "Deposit"));

        assertTrue(html.contains("href=\"/styles.css\""));
        assertTrue(html.contains("class=\"account-layout\""));
        assertTrue(html.contains("<li>1: Deposit</li>"));
        assertTrue(html.contains("action=\"/transactions\" method=\"post\""));
        assertTrue(html.contains("action=\"/transactions/update\" method=\"post\""));
        assertTrue(html.contains("action=\"/transactions/delete\" method=\"post\""));
        assertTrue(html.contains("name=\"txId\""));
        assertTrue(html.contains("name=\"description\""));
    }

    @Test
    public void rendersEmptyTransactionsMessage() throws Exception {
        assertTrue(renderTransactions(Map.of()).contains("You have no transactions to display."));
    }

    private String render(Map<String, String> transactions) throws Exception {
        TemplateEngine engine = createEngine();
        StringOutput output = new StringOutput();
        engine.render("jte/accountPage.jte", Map.of(
            "title", "Account test",
            "account", new UserAccount("test-id", "Test User", AppRole.CUSTOMER, 1234.5, transactions)
        ), output);
        return output.toString();
    }

    private String renderTransactions(Map<String, String> transactions) throws Exception {
        TemplateEngine engine = createEngine();
        StringOutput output = new StringOutput();
        engine.render("jte/transactionsPage.jte", Map.of("transactions", transactions), output);
        return output.toString();
    }

    private TemplateEngine createEngine() throws Exception {
        return TemplateEngine.create(
            new DirectoryCodeResolver(Path.of("src", "main", "resources")),
            temporaryFolder.newFolder().toPath(),
            ContentType.Html
        );
    }
}
