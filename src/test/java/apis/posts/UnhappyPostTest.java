package apis.posts;

import io.restassured.http.Method;
import models.post.PartialPost;
import models.post.PostResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.BaseTest;

import java.io.File;

public class UnhappyPostTest extends BaseTest {

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
    public void getSinglePostTest(){
        response = request
                .basePath("posts/{id}")
                .pathParams("id",2500)
                .request(Method.GET);
        Assert.assertEquals(response.getStatusCode(), 404);

    }
    @Test(groups = {regression, smoke})
    public void deletePostTest(){
        response = request
                .basePath("posts/{id}")
                .pathParams("id",2500)
                .request(Method.DELETE);
        Assert.assertEquals(response.getStatusCode(), 404);
    }
    @Test(groups = {regression, smoke})
    public void UpdatePostsTest(){
        final var post = PostResponse.generateRandomPost();

        response = request
                .basePath("posts/{id}")
                .pathParams("id",2700)
                .body(post)
                .request(Method.PUT);
        Assert.assertEquals(response.getStatusCode(), 404);


    }
    @Test(groups = {regression, smoke})
    public void partialUpdatePostsTest(){
        final var partialPost = PartialPost.generateRandomPartialPost();

        response = request
                .basePath("posts/{id}")
                .pathParams("id",2500)
                .body(partialPost)
                .request(Method.PATCH);
        Assert.assertEquals(response.getStatusCode(), 404);


    }

}
