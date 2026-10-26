package models.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;

public record Company(
        @JsonProperty("departament") String departament,
        @JsonProperty("name") String name,
        @JsonProperty("tile") String title
) {
public static Company generateRandomCompany(){
    final var faker = new Faker();
    final var department = faker.company().industry();
    final var name = faker.company().name();
    final var title = faker.job().title();

    return new Company(department,name,title);
}
}
