package org.ubdev.car.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.ubdev.car.dto.CarDto;
import org.ubdev.car.model.Car;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class CarByUserRepositoryImpl implements CarByUserRepository {
    private static final String SAVE_CAR_WITH_USER_EMAIL = """
            INSERT INTO cars(id, user_id, brand, model, year, color, registration_number)
            VALUES (:id, (SELECT id FROM users WHERE email = :email), :brand, :model, :year, :color, :registration_number)
            RETURNING id, user_id, brand, model, year, color, registration_number;
            """;

    private static final String FIND_ALL_BY_USER_EMAIL = """
            SELECT c.id, c.brand, c.model, c.year, c.color, c.registration_number FROM cars c
            JOIN users u ON c.user_id = u.id
            WHERE u.email = :email;
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public CarDto saveDocumentWithUserEmail(Car car, String userEmail) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", car.getId())
                .addValue("email", userEmail)
                .addValue("brand", car.getBrand())
                .addValue("model", car.getModel())
                .addValue("year", car.getYear())
                .addValue("color", car.getColor())
                .addValue("registration_number", car.getRegistrationNumber());

        return jdbcTemplate.queryForObject(SAVE_CAR_WITH_USER_EMAIL, params, getCarDtoMapper());
    }

    @Override
    public List<CarDto> findAllDocumentsByUserEmail(String userEmail) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", userEmail);

        return jdbcTemplate.query(FIND_ALL_BY_USER_EMAIL, params, getCarDtoMapper());
    }

    private RowMapper<CarDto> getCarDtoMapper() {
        return ((rs, rowNum) -> new CarDto(
                rs.getObject("id", UUID.class),
                rs.getString("brand"),
                rs.getString("model"),
                rs.getInt("year"),
                rs.getString("color"),
                rs.getString("registration_number")
        ));
    }
}
