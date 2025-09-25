package models.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;

public record Reactions(
        @JsonProperty("likes") int likes,
                @JsonProperty("dislikes") int dislikes

        ){
            public static Reactions generateRandomReactions(){
                final var faker = new Faker();
                final var likes = faker.number().numberBetween(0,5000);
                final var dislikes = faker.number().numberBetween(0,5000);

                return new Reactions(likes,dislikes);
            }
}
