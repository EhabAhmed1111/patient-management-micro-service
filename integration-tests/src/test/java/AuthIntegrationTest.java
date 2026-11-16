import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class AuthIntegrationTest {

    @BeforeAll
    static void setUp() {
        /*
        * we use local host instead of api-gateway
        * because each of the test will run as external request not from internal service
        * also we use 4004 because every request must ggo through api gateway*/
        RestAssured.baseURI = "http://localhost:4004";
    }

    @Test
    public void shouldReturnOkWithValidToken() {
        // given // arrange
        String loginPayload = """
                {
                "email": "testuser@test.com",
                "password": "password123"
                }
                """;

        // when // act
        // then // assert
        Response response = given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract().response();


        System.out.println("token: " + response.jsonPath().getString("token"));

    }


    @Test
    public void shouldReturnUnauthorizedOnInvalidLogin() {
        // given // arrange
        String loginPayload = """
                {
                "email": "invalid_user@test.com",
                "password": "wrongpassword"
                }
                """;

        // when // act
        // then // assert
      given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);



    }
}