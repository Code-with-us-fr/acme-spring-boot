package com.acme.api.user;

import com.acme.api.IntegrationTest;
import com.acme.api.user.domain.Address;
import com.acme.api.user.domain.User;
import com.acme.api.user.domain.UserRepository;
import com.acme.api.user.interfaces.UserRepresentation;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.acme.api.common.interfaces.TestingJwts.ADMIN_TOKEN;
import static com.acme.api.common.interfaces.TestingJwts.READER_TOKEN;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@IntegrationTest
public class UserIT {

    @Autowired
    UserRepository userRepository;

    @Test
    void shouldReturnEmptyListWithEmptyDatabase() {
        RestAssured.given()
                .auth().oauth2(READER_TOKEN)
                .get("/users")
                .then().statusCode(OK.value())
                .body("$", hasSize(0));
    }

    @Test
    void shouldReturnAllUsers() {
        User bipBip = bipBip();
        User vilCoyote = getVilCoyote();
        userRepository.saveAll(List.of(bipBip, vilCoyote));

        RestAssured.given()
                .auth().oauth2(READER_TOKEN)
                .when().get("/users")
                .then().statusCode(OK.value())
                .body("$", hasSize(2))
                .body("[0].email", equalTo(bipBip.getEmail()))
                .body("[0].city", equalTo(bipBip.getAddress().getCity()))
                .body("[1].email", equalTo(vilCoyote.getEmail()))
                .body("[1].city", equalTo(vilCoyote.getAddress().getCity()));
    }

    @Test
    void shouldRejectReaderCreatingUser() {
        RestAssured.given()
                .auth().oauth2(READER_TOKEN)
                .body(new UserRepresentation("Sam", "Yosemite", "sam.yosemite@acme.com", "45693", "San Fransisco"))
                .contentType(JSON)
                .when().post("/users")
                .then().statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldAcceptWriterCreatingUser() {
        RestAssured.given()
                .auth().oauth2(ADMIN_TOKEN)
                .body(new UserRepresentation("Sam", "Yosemite", "sam.yosemite@acme.com", "45693", "San Fransisco"))
                .contentType(JSON)
                .when().post("/users")
                .then().statusCode(OK.value());
    }

    private static User bipBip() {
        return new User()
                .setFirstName("Bip")
                .setLastName("Bip")
                .setEmail("bip@acme.com")
                .setAddress(new Address()
                        .setPostalCode("68963")
                        .setCity("Houston"));
    }

    private static User getVilCoyote() {
        return new User()
                .setFirstName("Vil")
                .setLastName("Coyote")
                .setEmail("vil@acme.com")
                .setAddress(new Address()
                        .setPostalCode("47532")
                        .setCity("Las Vegas"));
    }
}
