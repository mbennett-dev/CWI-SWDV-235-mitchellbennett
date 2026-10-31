package edu.cwi;

import io.javalin.Javalin;
import me.gosimple.nbvcxz.resources.Generator;

public class Main {
    public static void main(String[] args) {
        String pass1 = Generator.generatePassphrase("-", 5);
        System.out.println("Your new passphrase is: " + pass1);

        Javalin app = Javalin.create(config -> {
            config.routes.get("/hello/{name}", ctx -> {
                String name = ctx.pathParam("name");
                ctx.result("Hello, " + name);
            });

            config.routes.get("/hello/{name}/html", ctx -> {
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
        });

        app.start(7070);
    }
}