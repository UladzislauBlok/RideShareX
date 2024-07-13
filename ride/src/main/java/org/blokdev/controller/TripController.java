package org.blokdev.controller;

import lombok.RequiredArgsConstructor;
import org.blokdev.dto.CreateTripDto;
import org.blokdev.dto.JoinTripDecisionDto;
import org.blokdev.dto.TripDto;
import org.blokdev.service.TripService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ride")
@RequiredArgsConstructor
public class TripController {
    private final TripService tripService;

    @PostMapping
    public ResponseEntity<TripDto> createTrip(@RequestBody CreateTripDto dto, @RequestParam String ownerEmail) {
        TripDto tripDto = tripService.createTrip(dto, ownerEmail);
        return new ResponseEntity<>(tripDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<TripDto>> getTripsByRouteAndTime(@RequestParam String departureCity,
                                                                @RequestParam String arrivalCity,
                                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureTime,
                                                                @RequestParam int pageNum,
                                                                @RequestParam int pageSize) {
        Page<TripDto> trips = tripService.getTripsByRouteAndTime(departureCity, arrivalCity, departureTime, pageNum, pageSize);
        return new ResponseEntity<>(trips, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable UUID id) {
        tripService.deleteTrip(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{tripId}/join-requests")
    public ResponseEntity<Void> requestJoinTrip(@PathVariable UUID tripId, Principal principal) {
        tripService.requestJoinTrip(tripId, principal.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/join-requests/decision")
    public ResponseEntity<Void> processJoinTripDecision(@RequestBody JoinTripDecisionDto dto, Principal principal) {
        tripService.processJoinTripDecision(dto, principal.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{tripId}/start")
    public ResponseEntity<Void> startTrip(@PathVariable UUID tripId, Principal principal) {
        tripService.startTrip(tripId, principal.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{tripId}/end")
    public ResponseEntity<Void> endTrip(@PathVariable UUID tripId, Principal principal) {
        tripService.endTrip(tripId, principal.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/ratings")
    public ResponseEntity<Void> createRating(@RequestParam UUID ratedUserId,
                                             @RequestParam Integer ratingValue,
                                             Principal principal) {
        tripService.createRating(ratedUserId, ratingValue, principal.getName());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
