package org.ubdev.document.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.ubdev.document.dto.DocumentDto;
import org.ubdev.document.model.Document;
import org.ubdev.document.model.DocumentType;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class DocumentByUserRepositoryImpl implements DocumentByUserRepository {
    private static final String SAVE_DOCUMENT_WITH_USER_EMAIL = """
            INSERT INTO documents(id, user_id, type, number, creation_date)
            VALUES (:id, (SELECT id FROM users WHERE email = :email), :type, :number, :creation_date)
            RETURNING id, type, number, creation_date
            """;

    private static final String FIND_ALL_BY_USER_EMAIL = """
            SELECT d.id, d.type, d.number, d.creation_date FROM documents d
            JOIN users u ON d.user_id = u.id
            WHERE u.email = :email;
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;


    @Override
    public DocumentDto saveDocumentWithUserEmail(Document document, String userEmail) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", document.getId())
                .addValue("email", userEmail)
                .addValue("type", document.getType())
                .addValue("number", document.getNumber())
                .addValue("creation_date", document.getCreationDate());

        return jdbcTemplate.queryForObject(SAVE_DOCUMENT_WITH_USER_EMAIL, params, getDocumentDtoMapper());
    }

    @Override
    public List<DocumentDto> findAllDocumentsByUserEmail(String userEmail) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", userEmail);

        return jdbcTemplate.query(FIND_ALL_BY_USER_EMAIL, params, getDocumentDtoMapper());
    }

    private RowMapper<DocumentDto> getDocumentDtoMapper() {
        return (rs, rowNum) -> new DocumentDto(
                rs.getObject("id", UUID.class),
                rs.getObject("type", DocumentType.class),
                rs.getString("number"),
                rs.getDate("creation_date").toLocalDate());
    }
}
