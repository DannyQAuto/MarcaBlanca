package apis.users;

import io.restassured.http.Method;
import models.user.PartialUser;
import models.user.User;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.BaseTest;

import java.io.File;

public class UnhappyUserTests extends BaseTest {
    @BeforeMethod(alwaysRun = true)
    public void setUp(){
        request.baseUri("https://dummyjson.com/auth");
        final var requestBodyPath =
                "src/test/resources/requestBody/dummyjson/Auth/login.json";
        final var token = request
                .basePath("login")
                .body(new File(requestBodyPath))
                .request(Method.POST)
                .path("accessToken")
                .toString();
        request
                .auth().preemptive().oauth2(token);
    }
    @Test(groups = {regression, smoke})
    public void UpdateUsersTest() {
        final var user = User.generateRandomUser();

        response = request
                .basePath("users/{id}")
                .pathParams("id",2500)
                .body(user)
                .request(Method.PUT);
        Assert.assertEquals(response.getStatusCode(), 404);

    }
    @Test(groups = {regression, smoke})
    public void partialUpdateUserTest() {

        final var partialUser = PartialUser.generateRandomPartialUser();

        response = request
                .basePath("users/{id}")
                .pathParam("id",2500)
                .body(partialUser)
                .request(Method.PATCH);
        Assert.assertEquals(response.getStatusCode(), 404);

    }
}
