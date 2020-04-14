import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.*;

public class Jsonplchldr {

    private int postNumber;
    RequestSpecification request;

    @BeforeTest
    public void setUp() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
        request = given();
    }

    @Test
    public void tc01_getAllposts() {
        Response response = request.get("/posts");
        System.out.println("body of " + postNumber + " is: \n " + response.asString());
    }

    @Test
    public void tc02_restAssured_getBody_and_statusLine() {
        postNumber = 100;
        Response response = request.get("/posts/" + postNumber);
        System.out.println("body of " + postNumber + " is: \n " + response.asString());

        String actualStatusLine = response.getStatusLine();
        String expectedStatusLine = "HTTP/1.1 200 OK";
        Assert.assertEquals(actualStatusLine, expectedStatusLine, "Messages are not equal");
    }

    @Test
    public void tc03_wrong_statusCode() {
        Response response = request.get("/posts/99abc999");
        int actualStatus = response.getStatusCode();
        Assert.assertEquals(actualStatus, 404, "Status not as expected");
    }

    @Test
    public void tc04_getTitle() {
        Response response = request.get("/posts/1");
        JsonPath jsonPathEvaluator = response.jsonPath();
        String title = jsonPathEvaluator.get("title");
        System.out.println("Title is: \"" + title + "\"");
        Assert.assertEquals("sunt aut facere repellat provident occaecati excepturi optio reprehenderit", title);
    }

    @Test
    public void tc5_getHighestNumberOfUserID() {
        String response = request.get("/posts").asString();
        JsonPath js = new JsonPath(response);
        List<Integer> list = js.getList("userId");
        System.out.println(Collections.max(list));
    }
}

