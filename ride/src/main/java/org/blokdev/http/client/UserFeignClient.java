package org.blokdev.http.client;

import feign.Headers;
import org.blokdev.model.JoinTripRequestData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient("user-microservice")
@Headers("System-Request:true")
public interface UserFeignClient {
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/api/v1/users/trip",
            consumes = "application/json"
    )
    JoinTripRequestData getDataForJoinTripRequest(@RequestParam("email") String email, @RequestParam("ownerId") UUID id);
}
