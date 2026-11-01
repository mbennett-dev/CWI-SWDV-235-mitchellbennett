package cwi.edu;

import io.javalin.Javalin;
public class Main 
{
    public static void main( String[] args )
    {
        Javalin app = Javalin.create().start(7070);

        app.get("/hello/{name}", ctx -> {
            String name = ctx.pathParam("name");
            ctx.result("Hello, " + name);
        });

        app.get("/hello/{name}/html", ctx -> {
            String name = ctx.pathParam("name");
            
            String htmlContent = "<html>\n" +
                    "<head>\n" +
                    "    <title>Javalin Intro</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <h1>Hello, " + name + "</h1>\n" +
                    "</body>\n" +
                    "</html>";
            
            ctx.html(htmlContent);
        });
    }
}