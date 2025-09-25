package models.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;

public record PartialProduct (
    @JsonProperty("title") String title,
    @JsonProperty("description") String description

            ){
    public static PartialProduct generateRandomPartialProduct(){
        final var faker = new Faker();

        final var title = faker.backToTheFuture().character();
        final var description= faker.backToTheFuture().quote();

    return new PartialProduct(title, description);

    }
}
