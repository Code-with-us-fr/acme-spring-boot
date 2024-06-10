package com.acme.api.user;

import com.acme.api.IntegrationTest;
import com.acme.api.user.domain.User;
import com.acme.api.user.domain.UserRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@IntegrationTest
public class UserIT {

    @Autowired
    UserRepository userRepository;

    @Test
    void shouldReturnEmptyListWithEmptyDatabase() {
        RestAssured.when().get("/users")
                .then().statusCode(HttpStatus.OK.value())
                .body("$", hasSize(0));
    }

    @Test
    void shouldReturnAllUsers() {
        User bipBip = bipBip();
        User vilCoyote = getVilCoyote();
        userRepository.saveAll(List.of(bipBip, vilCoyote));

        RestAssured.when().get("/users")
                .then().statusCode(HttpStatus.OK.value())
                .body("$", hasSize(2))
                .body("[0].email", equalTo(bipBip.getEmail()))
                .body("[1].email", equalTo(vilCoyote.getEmail()));
    }

    private static User bipBip() {
        return new User()
                .setFirstName("Bip")
                .setLastName("Bip")
                .setEmail("bip@acme.com");
    }

    private static User getVilCoyote() {
        return new User()
                .setFirstName("Vil")
                .setLastName("Coyote")
                .setEmail("vil@acme.com");
    }
}
