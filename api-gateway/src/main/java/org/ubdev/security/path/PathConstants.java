package org.ubdev.security.path;

import java.util.Set;

public class PathConstants {
    public static final Set<String> PUBLIC_PATH = Set.of(
            "POST|/api/v1/jwt/tokens",
            "POST|/api/v1/users"
    );

    public static final Set<String> REFRESH_TOKEN_PATH = Set.of(
            "POST|/api/v1/jwt/refresh",
            "POST|/api/v1/jwt/logout"

    );
    private static final Set<String> ACCESS_TOKEN_PATH = Set.of(

    );
}
