package org.blokdev.kafka.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record AcceptUserMessage (
        @JsonProperty("accept_username")
        String acceptUsername,

        @JsonProperty("accept_user_id")
        UUID acceptUserId,

        @JsonProperty("accept_link_id")
        UUID acceptLinkId
) {
}
