package apis.users;

import io.restassured.http.Method;
import models.user.PartialUser;
import models.user.User;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.BaseTest;

public class NoAuthUserTest extends BaseTest {
    @BeforeMethod(alwaysRun = true)
    public void setUp(){
        request.baseUri("https://dummyjson.com/auth");


    }
    @Test(groups = {regression, smoke})
    public void UpdateUsersTest() {
        final var user = User.generateRandomUser();

        response = request
                .basePath("users/{id}")
                .pathParams("id",5)
                .body(user)
                .request(Method.PUT);
        Assert.assertEquals(response.getStatusCode(), 401);

    }
    @Test(groups = {regression, smoke})
    public void partialUpdateUserTest() {

        final var partialUser = PartialUser.generateRandomPartialUser();

        response = request
                .basePath("users/{id}")
                .pathParam("id",5)
                .body(partialUser)
                .request(Method.PATCH);
        Assert.assertEquals(response.getStatusCode(), 401);

    }

}
