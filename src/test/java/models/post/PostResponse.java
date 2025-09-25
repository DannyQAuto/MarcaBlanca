package models.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;

import java.util.List;

public record PostResponse(
        @JsonProperty("id") int id,
        @JsonProperty("title") String title,
        @JsonProperty("body") String body,
        @JsonProperty("tags") List<String> tags,
        @JsonProperty("reactions") Reactions reactions,
        @JsonProperty("views") int views,
        @JsonProperty("userId") int userId
) {
    public static PostResponse generateRandomPost() {
        final var faker = new Faker();
        return new PostResponse(
                faker.number().numberBetween(1, 1000),  // id
                faker.lorem().sentence(),               // title
                faker.lorem().paragraph(),              // body
                faker.lorem().words(5),                 // tags
                Reactions.generateRandomReactions(),    // reactions
                faker.number().numberBetween(0, 1000),  // views
                faker.number().numberBetween(1, 16)     // userId
        );
    }
}