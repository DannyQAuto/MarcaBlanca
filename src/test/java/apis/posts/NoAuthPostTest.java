package apis.posts;

import io.restassured.http.Method;
import models.post.PartialPost;
import models.post.PostResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.BaseTest;

import java.util.Map;

public class NoAuthPostTest extends BaseTest {
    @BeforeMethod(alwaysRun = true)
    public void setUp(){
        request.baseUri("https://dummyjson.com/auth");
        final var requestBodyPath =
                "src/test/resources/requestBody/dummyjson/Auth/login.json";

    }
    @Test(groups = {regression, smoke})
    public void getAllPostTest(){
        response = request
                .basePath("posts")
                .request(Method.GET);
        Assert.assertEquals(response.getStatusCode(), 401);

    }
    @Test(groups = {regression, smoke})
    public void getSinglePostTest(){
        response = request
                .basePath("posts/{id}")
                .pathParams("id",3)
                .request(Method.GET);
        Assert.assertEquals(response.getStatusCode(), 401);
    }
    @Test(groups = {regression, smoke})
    public void deletePostTest(){
        response = request
                .basePath("posts/{id}")
                .pathParams("id",3)
                .request(Method.DELETE);
        Assert.assertEquals(response.getStatusCode(), 401);

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
        Assert.assertEquals(response.getStatusCode(), 401);

    }
    @Test(groups = {regression, smoke})
    public void createPostsTest(){

        final var post = PostResponse.generateRandomPost();

        response = request
                .basePath("posts/add")
                .body(post)
                .request(Method.POST);
        Assert.assertEquals(response.getStatusCode(), 401);
    }
    @Test(groups = {regression, smoke})
    public void UpdatePostsTest(){
        final var post = PostResponse.generateRandomPost();

        response = request
                .basePath("posts/{id}")
                .pathParams("id",3)
                .body(post)
                .request(Method.PUT);
        Assert.assertEquals(response.getStatusCode(), 401);


    }
    @Test(groups = {regression, smoke})
    public void partialUpdatePostsTest(){
        final var partialPost = PartialPost.generateRandomPartialPost();

        response = request
                .basePath("posts/{id}")
                .pathParams("id",3)
                .body(partialPost)
                .request(Method.PATCH);
        Assert.assertEquals(response.getStatusCode(), 401);


    }
}


