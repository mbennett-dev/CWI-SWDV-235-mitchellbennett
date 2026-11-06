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
    public void rendersAccountAndTransactionsWithForms() throws Exception {
        String html = render(Map.of("1", "Deposit"));

        assertTrue(html.contains("<title>Account test</title>"));
        assertTrue(html.contains("Test User"));
        assertTrue(html.contains("$1,234.50"));
        assertFalse(html.contains("$$"));
        assertTrue(html.contains("<li>1: Deposit</li>"));
        assertTrue(html.contains("action=\"/transactions\" method=\"post\""));
        assertTrue(html.contains("action=\"/transactions/update\" method=\"post\""));
        assertTrue(html.contains("action=\"/transactions/delete\" method=\"post\""));
        assertTrue(html.contains("name=\"txId\""));
        assertTrue(html.contains("name=\"description\""));
    }

    @Test
    public void rendersEmptyTransactionsMessage() throws Exception {
        assertTrue(render(Map.of()).contains("You have no transactions to display."));
    }

    private String render(Map<String, String> transactions) throws Exception {
        TemplateEngine engine = TemplateEngine.create(
            new DirectoryCodeResolver(Path.of("src", "main", "resources")),
            temporaryFolder.newFolder().toPath(),
            ContentType.Html
        );
        StringOutput output = new StringOutput();
        engine.render("jte/accountPage.jte", Map.of(
            "title", "Account test",
            "account", new UserAccount("Test User", 1234.5, transactions)
        ), output);
        return output.toString();
    }
}
