package models.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;

public record Address (
    @JsonProperty("address") String address,
    @JsonProperty("city") String city,
@JsonProperty("state") String state,
@JsonProperty("country") String country,
@JsonProperty("coordinates") Coordinates coordinates
){
    public static Address generateRandomAddress(){
        final var faker = new Faker();
            final var address = faker.address().streetAddress();
        final var city = faker.address().city();
            final var state = faker.address().state();
            final var country = faker.address().country();
            final var coordinates = Coordinates.generateRandomCoordinates();

            return new Address(address, city ,state,country,coordinates);
        }
    }

