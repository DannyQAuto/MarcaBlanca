package models.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;

import java.util.List;

public record User(
        @JsonProperty("firstName") String firstName,
                @JsonProperty("lasName") String lastName,
        @JsonProperty("age") int age,
        @JsonProperty("gender") String gender,
        @JsonProperty("email") String email,
        @JsonProperty("phone") String phone,
        @JsonProperty("role") String role,
        @JsonProperty("address") Address address,
        @JsonProperty("company") Company company

        ){
    public static User generateRandomUser(){
        final var faker = new Faker();

        final var firstName = faker.name().firstName();
        final var lastName = faker.name().lastName();
        final var ager = faker.number().numberBetween(20,80);
        final var gender = List.of("male", "female").get(faker.number().numberBetween(0, 2));
        final var email = faker.internet().emailAddress();
        final var phone = faker.phoneNumber().phoneNumber();
        final var role = faker.job().position();
        final var address = Address.generateRandomAddress();
        final var company = Company.generateRandomCompany();

        return new User(firstName,lastName,ager,gender,email,phone,role,address,company);


    }

}