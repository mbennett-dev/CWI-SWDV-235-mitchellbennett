package edu.cwi;

import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.http.staticfiles.Location;

public class Main {
    public static void main(String[] args) {
        Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);

            config.routes.apiBuilder(() -> {
                ApiBuilder.get("/", ctx -> ctx.redirect("/index.html"));

                ApiBuilder.get("/account", ctx -> ctx.redirect("/account.html"));
                ApiBuilder.post("/account", ctx -> ctx.redirect("/account.html"));
                ApiBuilder.get("/logout", ctx -> ctx.redirect("/"));

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
