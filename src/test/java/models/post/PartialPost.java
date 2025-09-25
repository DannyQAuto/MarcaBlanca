package models.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;

public record PartialPost (
    @JsonProperty("title") String title,
    @JsonProperty("body") String body
){
    public static PartialPost generateRandomPartialPost(){
        final var faker = new Faker();
        final var title = faker.lorem().word();
        final var body = faker.lorem().sentence(10);

        return new PartialPost(title,body);
    }
}

