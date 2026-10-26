package apis.posts;

import io.restassured.http.Method;
import io.restassured.module.jsv.JsonSchemaValidator;
import models.post.PartialPost;
import models.post.PostResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.BaseTest;

import java.io.File;
import java.util.Map;

public class posts extends BaseTest {
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
    @Test (groups = {regression, smoke})
    public void getAllPostTest(){
        response = request
                .basePath("posts")
                .request(Method.GET);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 1500);
    }
    @Test(groups = {regression, smoke})
    public void getSinglePostTest(){
        response = request
                .basePath("posts/{id}")
                .pathParams("id",3)
                .request(Method.GET);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 1500);
final var path = "C:/Users/Daniel Espiritu/IdeaProjects/RestAsured/src/test/resources/schemas/post.json";
response.then().body(JsonSchemaValidator.matchesJsonSchema(new File(path)));

    }
    @Test(groups = {regression, smoke})
    public void deletePostTest(){
        response = request
                .basePath("posts/{id}")
                .pathParams("id",3)
                .request(Method.DELETE);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 1500);

    }
    @Test(groups = {regression, smoke})
    public void sortPostsTest(){
        final var queryParamMap = Map.of(
                "sortBy", "views",
                "order", "desc"
        );
                response = request
                        .basePath("posts/{id}")
                        .pathParams("id",3)
                        .request(Method.DELETE);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 1500);

    }
    @Test(groups = {regression, smoke})
    public void createPostsTest(){

       final var post = PostResponse.generateRandomPost();

       response = request
                .basePath("posts/add")
                .body(post)
                .request(Method.POST);
        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertTrue(response.getTime() < 1500);


        final var postResponse = response.body().as(PostResponse.class);

        softAssert.assertTrue(postResponse.id()>0);
        softAssert.assertTrue(postResponse.reactions().likes()>0);
        softAssert.assertTrue(postResponse.reactions().dislikes()>0);
        softAssert.assertAll();
    }
    @Test(groups = {regression, smoke})
    public void UpdatePostsTest(){
        final var post = PostResponse.generateRandomPost();

       response = request
                .basePath("posts/{id}")
                .pathParams("id",3)
                .body(post)
                .request(Method.PUT);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 1500);

    }
    @Test(groups = {regression, smoke})
    public void partialUpdatePostsTest(){
        final var partialPost = PartialPost.generateRandomPartialPost();

        response = request
                .basePath("posts/{id}")
                .pathParams("id",3)
                .body(partialPost)
                .request(Method.PATCH);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 1500);

    }
}
