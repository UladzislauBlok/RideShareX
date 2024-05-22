package org.ubdev.jwt.repository;

public class TokenSqlQueries {
    public static final String IS_TOKEN_BANNED_QUERY = """
            SELECT EXISTS(SELECT id FROM deactivated_token WHERE id = ?)""";

    public static final String BAN_TOKEN_QUERY = """
            INSERT INTO deactivated_token (id, c_keep_until) VALUES (?, ?)""";
}
