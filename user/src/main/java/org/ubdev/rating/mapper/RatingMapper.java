package org.ubdev.rating.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.ubdev.kafka.model.rating.CreateRatingMessage;
import org.ubdev.rating.model.Rating;
import org.ubdev.user.model.User;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    @Mappings({
            @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())"),
            @Mapping(target = "ratedUser", source = "ratedUser"),
            @Mapping(target = "ratingUser", source = "ratingUser")
    })
    Rating mapCreateRatingMessageToRating(CreateRatingMessage createRatingMessage, User ratedUser, User ratingUser );
}
