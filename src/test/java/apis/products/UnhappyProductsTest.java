package apis.products;

import io.restassured.http.Method;
import models.product.Product;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.BaseTest;

import java.io.File;

public class UnhappyProductsTest extends BaseTest {
    @BeforeMethod(alwaysRun = true)
    public void setUp(){

        request.baseUri("https://dummyjson.com/auth");
        final var requestBodyPath =
                "src/test/resources/requestBody/dummyjson/Auth/login.json";
        final var token = request
                .basePath(  "login")
                .body(new File(requestBodyPath))
                .request(Method.POST)
                .path("accessToken")
                .toString();
        request
                .auth().preemptive().oauth2(token);
    }

    @Test(groups = {regression})
    public void getSingProductTest(){
        response = request
                .basePath("products/{id}")
                .pathParams("id",2500)
                .request(Method.GET);
        Assert.assertEquals(response.getStatusCode(), 404);


    }

    @Test (groups = {regression})
    public  void deleteProductTest(){
        response =  request
                .basePath("products/{id}")
                .pathParams("id",2500)
                .request(Method.DELETE);
        Assert.assertEquals(response.getStatusCode(), 404);

    }
        @Test(groups = {regression})
        public  void updateProductTest(){
            final  var product = Product.generateRandomProduct();
            response = request
                    .basePath("products/{id}")
                    .pathParam("id",2500)
                    .body(product)
                    .request(Method.PUT);
            Assert.assertEquals(response.getStatusCode(), 404);


    }


}
