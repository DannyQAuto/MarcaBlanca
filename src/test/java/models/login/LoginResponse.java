package models.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;

import java.util.List;

public record LoginResponse(
    @JsonProperty("accessToken") String accessToken,
    @JsonProperty("refreshToken") String refreshToken,
    @JsonProperty("id") int id,
    @JsonProperty("username")String username,
    @JsonProperty("email") String Email,
    @JsonProperty("firstName") String firstName,
    @JsonProperty("lastName") String lastName,
    @JsonProperty("gender") String gender,
    @JsonProperty("image") String image

)
{
    public static LoginResponse generateRandomLogin(){
        final var faker = new Faker();
        final var accessToken = faker.lorem().characters(360);
        final var refreshToken = faker.lorem().characters(360);
        final var id = faker.number().numberBetween(1,15);
        final var username = faker.name().username();
        final var email = faker.internet().emailAddress();
        final var fistName = faker.name().firstName();
        final var lastName = faker.name().lastName();
        final var gender = List.of("male","female").get(faker.number().numberBetween(0,1));
        final var image = faker.internet().image();

        return new LoginResponse(accessToken,refreshToken,id,username,email,fistName,lastName,gender,image);


    }
}

