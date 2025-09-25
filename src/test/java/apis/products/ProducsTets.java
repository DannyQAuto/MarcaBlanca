package apis.products;

import io.restassured.http.Method;
import io.restassured.module.jsv.JsonSchemaValidator;
import models.product.Product;
import models.product.ProductResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.BaseTest;

import java.io.File;
import java.util.Map;

public class ProducsTets extends  BaseTest{
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


        @Test(groups = {regression, smoke})
        public void getAllProductsTest(){
            response = request
                    .basePath("products")
                    .request(Method.GET);
            Assert.assertEquals(response.getStatusCode(), 200);
            Assert.assertTrue(response.getTime() < 1500);

        }
        @Test (groups = {regression, smoke})

    public void getSingProductTest(){
        response = request
                .basePath("products/{id}")
                .pathParams("id",2)
                .request(Method.GET);
            Assert.assertEquals(response.getStatusCode(), 200);
            Assert.assertTrue(response.getTime() < 1500);
        final var path = "src/test/resources/schemas/product.json";
        response.then().body(JsonSchemaValidator.matchesJsonSchema( new File(path)));

        }
        @Test (groups = {regression, smoke})
    public  void deleteProductTest(){
       response =  request
                .basePath("products/{id}")
                .pathParams("id",2)
                .request(Method.DELETE);
            Assert.assertEquals(response.getStatusCode(), 200);
            Assert.assertTrue(response.getTime() < 1500);
        }
    @Test (groups = {regression, smoke})
    public  void sortProductsTest(){
        final var queryParamMap = Map.of(
                "sortBy","price",
                "order", "asc"
        );
        response = request
                .basePath("products")
                .queryParams(queryParamMap)
                .request(Method.GET);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 1500);
    }
    @Test (groups = {regression, smoke})
    public  void searchProductsTest(){

       response = request
                .basePath("products")
                .queryParams("q","kitchen")
                .request(Method.GET);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 1500);
    }
    @Test(groups = {regression, smoke})
    public  void createProductTest(){
        final  var product = Product.generateRandomProduct();
        response = request
                .basePath("products/add")
                .body(product)
                .request(Method.POST);
        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertTrue(response.getTime() < 1500);

         final var productResponse = response.body().as(ProductResponse.class);
         softAssert.assertTrue(productResponse.id()>0);
         softAssert.assertTrue(productResponse.rating()<=5.00);
         softAssert.assertTrue(productResponse.rating()>=0.00);
         softAssert.assertAll();
    }
    @Test(groups = {regression, smoke})
    public  void updateProductTest(){
        final  var product = Product.generateRandomProduct();
        response = request
                .basePath("products/{id}")
                .pathParam("id",3)
                .body(product)
                .request(Method.PUT);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 1500);
    }
    }

