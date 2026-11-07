package cwi.edu;

// AppRole.java
import io.javalin.security.RouteRole;

public enum AppRole implements RouteRole { NONE, CUSTOMER, ADMIN }