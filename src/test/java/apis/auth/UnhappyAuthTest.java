package apis.auth;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.BaseTest;

import java.io.File;

public class UnhappyAuthTest extends BaseTest {
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        request.baseUri("https://dummyjson.com");
    }

    @Test(groups = {regression})
    public void emptyCredentialsLoginTest() {
        final var requestBodyPath =
                "src/test/resources/requestBody/dummyjson/Auth/emptyCredentials.json";

        // Envía la solicitud y obtén la respuesta
        Response response = request
                .contentType(ContentType.JSON)  // Asegura el tipo de contenido
                .basePath("/auth/login")       // Usa "/" al inicio
                .body(new File(requestBodyPath))
                .request(Method.POST);

        // Valida la respuesta
        Assert.assertEquals(response.getStatusCode(), 200);
    }
    @Test(groups = {regression})
    public void invalidCredentialsLoginTest() {
        final var requestBodyPath =
                "src/test/resources/requestBody/dummyjson/Auth/invalidCredentials.json";

        // Envía la solicitud y obtén la respuesta
            response = request

                .basePath("/auth/login")       // Usa "/" al inicio
                .body(new File(requestBodyPath))
                .request(Method.POST);

        // Valida la respuesta
        Assert.assertEquals(response.getStatusCode(), 400);
    }
}