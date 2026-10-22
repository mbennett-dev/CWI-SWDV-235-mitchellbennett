package edu.cwi;

import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.routes.apiBuilder(() -> {
                path("/hello", () -> {
                    path("{name}", () -> {
                        get(ctx -> {
                            String name = ctx.pathParam("name");
                            ctx.result("Hello, " + name);
                        });

                        path("html", () -> {
                            get(ctx -> {
                                String name = ctx.pathParam("name");
                                ctx.html("<html>\n"
                                        + "   <head>\n"
                                        + "    <title>Javalin Intro</title>\n"
                                        + "   </head>\n"
                                        + "   <body>\n"
                                        + "    <h1>Hello, " + name + "</h1>\n"
                                        + "   </body>\n"
                                        + "</html>");
                            });
                        });
                    });
                });
            });
        }).start(7000);

        System.out.println("Javalin app started on http://localhost:" + app.port());
    }
}