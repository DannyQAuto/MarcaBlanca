package models.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;

public record Product(
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("category") String category,
        @JsonProperty("price") double price,
        @JsonProperty("rating") double rating,
        @JsonProperty("stock") int stock
) {
    public static Product generateRandomProduct() {
        final var faker = new Faker();
        final var title = faker.food().ingredient();
        final var description = faker.lorem().sentence(10);
        final var category = faker.app().name();
        final var price = faker.number().randomDouble(2, 10, 100);
        final var rating = faker.number().randomDouble(2, 0, 5);
        final var stock = faker.number().numberBetween(1, 100);

        return new Product(title, description, category, price, rating, stock);
    }
}