package apis.products;

import io.restassured.http.Method;
import models.product.Product;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.BaseTest;

import java.util.Map;

public class NoAuthProductTest extends BaseTest {
    @BeforeMethod(alwaysRun = true)
    public void setUp(){

        request.baseUri("https://dummyjson.com/auth");

    }
    @Test(groups = {regression, smoke})
    public void getAllProductsTest(){
        response = request
                .basePath("products")
                .request(Method.GET);
        Assert.assertEquals(response.getStatusCode(), 401);


    }
    @Test (groups = {regression, smoke})
    public void getSingProductTest(){
        response = request
                .basePath("products/{id}")
                .pathParams("id",2)
                .request(Method.GET);
        Assert.assertEquals(response.getStatusCode(), 401);


    }
    @Test (groups = {regression, smoke})
    public  void deleteProductTest(){
        response =  request
                .basePath("products/{id}")
                .pathParams("id",2)
                .request(Method.DELETE);
        Assert.assertEquals(response.getStatusCode(), 401);

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
        Assert.assertEquals(response.getStatusCode(), 401);

    }
    @Test (groups = {regression, smoke})
    public  void searchProductsTest(){

        response = request
                .basePath("products")
                .queryParams("q","kitchen")
                .request(Method.GET);
        Assert.assertEquals(response.getStatusCode(), 401);

    }
    @Test(groups = {regression, smoke})
    public  void createProductTest(){
        final  var product = Product.generateRandomProduct();
        response = request
                .basePath("products/add")
                .body(product)
                .request(Method.POST);
        Assert.assertEquals(response.getStatusCode(), 401);

    }
    @Test(groups = {regression, smoke})
    public  void updateProductTest(){
        final  var product = Product.generateRandomProduct();
        response = request
                .basePath("products/{id}")
                .pathParam("id",3)
                .body(product)
                .request(Method.PUT);
        Assert.assertEquals(response.getStatusCode(), 401);

    }
}


