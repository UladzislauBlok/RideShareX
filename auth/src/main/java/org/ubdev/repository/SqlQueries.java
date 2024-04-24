package org.ubdev.repository;

public class SqlQueries {
    public static final String IS_TOKEN_BANNED_QUERY = """
            SELECT EXISTS(SELECT id FROM deactivated_token WHERE id = ?)""";

    public static final String GET_USER_BY_EMAIL_QUERY = """
            SELECT email, password FROM users WHERE email = ?""";

    public static final String GET_USER_AUTHORITY_BY_USER_ID_QUERY = """
            SELECT a.authority FROM authority a\s
            JOIN user_authority ua ON a.id = ua.authority_id\s
            JOIN users u ON u.id = ua.user_id\s
            WHERE u.email = ?""";
}
