package org.ubdev.user.repository;

public class UserSqlQueries {
    public static final String GET_USER_BY_EMAIL_QUERY = """
            SELECT email, password FROM users WHERE email = ?""";

    public static final String GET_USER_AUTHORITY_BY_USER_ID_QUERY = """
            SELECT a.authority FROM authority a\s
            JOIN user_authority ua ON a.id = ua.authority_id\s
            JOIN users u ON u.id = ua.user_id\s
            WHERE u.email = ?""";

    public static final String INSERT_USER_QUERY = """
            INSERT INTO users(id, email, password, is_email_confirmed) VALUES (?, ?, ?, ?)
            """;

    public static final String ADD_AUTHORITY_TO_USER_QUERY = """
            INSERT INTO user_authority(user_id, authority_id) VALUES (?,\s
            (SELECT id FROM authority WHERE authority = 'ROLE_USER'))
            """;

    public static final String UPDATE_USER_EMAIL_QUERY = """
            UPDATE users SET email = ?\s
            WHERE email = ?
            """;

    public static final String DELETE_USER_BY_EMAIL_QUERY = """
            DELETE FROM users\s
            WHERE email = ?
            """;

    public static final String DELETE_USER_BY_ID_QUERY = """
            DELETE FROM users\s
            WHERE id = ?
            """;
}
