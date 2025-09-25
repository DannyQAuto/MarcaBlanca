package models.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;

public record Coordinates(
        @JsonProperty("lat") double lat,
        @JsonProperty("lng") double lng
) {
    public static Coordinates generateRandomCoordinates(){
        final var faker = new Faker();
        final var lat = faker.number().randomDouble(5,0,500);
        final var lng = faker.number().randomDouble(5,0,500);
        return new Coordinates(lat,lng);
    }
}
