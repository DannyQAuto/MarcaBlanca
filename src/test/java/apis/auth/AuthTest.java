package apis.auth;

import io.restassured.http.Method;
import models.login.LoginResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.BaseTest;

import java.io.File;

public class AuthTest extends BaseTest {
    @BeforeMethod(alwaysRun = true)
    public void setUp(){
        request.baseUri("https://dummyjson.com");

    }
    @Test(groups = {regression, smoke})
    public void loginTest(){
        final var requestBodyPath = "src/test/resources/requestBody/dummyjson/Auth/login.json";
        response = request
                .basePath("auth/login")
                .body(new File(requestBodyPath))
                .request(Method.POST);

        Assert.assertEquals(response.getStatusCode(), 200);

        Assert.assertTrue(response.getTime() < 18500);

        final var loginResponse = response.body().as(LoginResponse.class);

        softAssert.assertTrue(loginResponse.id() >0);
        softAssert.assertEquals(loginResponse.accessToken().length(),360);
        softAssert.assertEquals(loginResponse.refreshToken().length(),360);
        softAssert.assertAll();
    }
}
