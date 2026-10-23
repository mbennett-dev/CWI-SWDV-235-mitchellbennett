package edu.cwi;

import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;

public class Main {
    public static void main(String[] args) {
        Javalin.create(config -> {
            config.routes.apiBuilder(() -> {
                ApiBuilder.get("/hello/{name}", ctx -> {
                    String name = ctx.pathParam("name");
                    ctx.result("Hello, " + name);
                });

                ApiBuilder.get("/hello/{name}/html", ctx -> {
                    String name = ctx.pathParam("name");
                    String html = "<html>\n"
                            + "   <head>\n"
                            + "    <title>Javalin Intro</title>\n"
                            + "   </head>\n"
                            + "   <body>\n"
                            + "    <h1>Hello, " + name + "</h1>\n"
                            + "   </body>\n"
                            + "</html>";
                    ctx.contentType("text/html");
                    ctx.result(html);
                });
            });
        }).start(7000);
    }
}
