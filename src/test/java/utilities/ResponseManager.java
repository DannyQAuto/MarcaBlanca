package utilities;
import io.qameta.allure.Step;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import utilities.Logs;

import java.io.File;


    public class ResponseManager {
        private static Response response;


    @Step("Verificando el status code")
    public static void verifyStatusCode(int expectedStatusCode){
        Logs.info("Verificando el status code");
        Assert.assertEquals(response.getStatusCode(),expectedStatusCode);
    }


    @Step("Verificando el response time")
    public static void verifyResponseTime(int maxResponseTime){
        Logs.info("Verificando el response time");
        Assert.assertTrue(response.getTime() < maxResponseTime);
    }


    @Step("Haciendo el schema validation")
    public static void doSchemaValidation(String schemaPath){
        Logs.info("Haciendo el schema validation");
        response
                .then()
                .body(JsonSchemaValidator.matchesJsonSchema(new File(schemaPath)));
    }

    public static <T> T getResponseBody(Class<T> clazz){
        return response.body().as(clazz);
    }

    public static String getPathAsString(String path){
        return response.path(path).toString();
    }

        public static void setResponse(Response response) {
            ResponseManager.response = response;
        }
}

