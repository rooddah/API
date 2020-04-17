import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import static io.restassured.RestAssured.given;

public class JsonServerLocalTests {

    @BeforeTest
    public void setUp() {
        RestAssured.baseURI = "http://localhost:3000";
    }
    @Test
    public void tc01_get_email_of_user() {
        String user = "Sophie Loren";
        String email = "";
        String response = given().log().all()
                .when().get("comments")
                .then().assertThat().statusCode(200).extract().asString();

        JsonPath js = new JsonPath(response);
        int number = js.getList("id").size();

        List<String> list = js.getList("postId");
        for (int i = 0; i < number; i++) {
            String title = js.get("[" + i + "].name");
            if (title.equals(user)) {
                email = js.get("[" + i + "].email");
            }
            System.out.println(email);
        }
    }

    @Test
    public void tc02_multiply_and_sumAll() {
        int expectedSum = 123; //hardcoded value, therefore there might be fails due to db.json dynamically change. This test is to show logic, not *exact* assertion
        String response = given().log().all()
                .when().get("comments")
                .then().assertThat().statusCode(200).extract().asString();

        JsonPath js = new JsonPath(response);
        int number = js.getInt("size()");
        int sum = 0;
        for (int i = 0; i < number; i++) {
            int id = js.get("[" + i + "].id");
            int postId = js.get("[" + i + "].postId");
            int mult = id * postId;
            System.out.println("For entry '" + js.get("[" + i + "].name") + "' the multiplied number is: " + mult);
            sum += mult;
        }
        System.out.println(sum);
        Assert.assertEquals(sum, expectedSum, "Numbers do not match");
    }

    @Test(dataProvider = "body")
    public void tc03_post(String name, String email, String body, int postId) {
        String response = given().log().all().contentType(ContentType.JSON)
                .body(PayloadLocal.addComment(name, email, body, postId))
                .when().post("comments")
                .then().log().all().assertThat().statusCode(201).extract().response().asString();
        System.out.println(response);
    }

    @Test
    public void tc04_post_update_check_2() {
        String expBody = "Make America fast again";

        String response = given().log().all().contentType(ContentType.JSON)
                .body(PayloadLocal.addComment())
                .when().post("comments")
                .then().log().all().assertThat().statusCode(201).extract().response().asString();

        JsonPath js = new JsonPath(response);
        String id = js.getString("id");
        int postId = js.getInt("postId");

        given().log().all().contentType(ContentType.JSON)
                .body(PayloadLocal.addComment(null, null, expBody, postId))
                .when().put("/comments/" + id)
                .then().log().all().assertThat().statusCode(200);

        String updResponse = given().log().all()
                .when().get("/comments/" + id)
                .then().assertThat().log().all().statusCode(200).extract().response().asString();

        JsonPath js2 = new JsonPath(updResponse);
        String actBody = js2.getString("body");

        Assert.assertEquals(actBody, expBody);
    }

    @Test
    public void tc05_loadingPostFromFile() throws IOException {
        String response = given().log().all()
                .when().get("comments")
                .then().assertThat().statusCode(200).extract().asString();

        JsonPath js = new JsonPath(response);
        int initialNumOfComments = js.getInt("size()");
        System.out.println("There are " + initialNumOfComments + " comments before the update");

        String response2 = given().log().all().contentType(ContentType.JSON)
                .body(generateStrFromRsrc("D:\\automation\\resources\\json\\commentsJson.json"))
                .when().post("comments")
                .then().log().all().assertThat().statusCode(201).extract().response().asString();

        JsonPath js2 = new JsonPath(response2);
        int numOfNewComments = js2.getInt("size");
        System.out.println(numOfNewComments + " new comments were added");

        String response3 = given().log().all()
                .when().get("comments")
                .then().assertThat().statusCode(200).extract().asString();

        JsonPath js3 = new JsonPath(response3);
        int finalNumOfComments = js3.getInt("size()");
        System.out.println("So the final number of comments is now " + finalNumOfComments);
        int desiredSum = initialNumOfComments + numOfNewComments;
        Assert.assertEquals(finalNumOfComments, desiredSum);
    }

    @DataProvider(name = "body")
    public Object[][] getBody() {
        return new Object[][]{{"Jeff Bridges", "jb@film.pl", "Body example", 33},
                {"Will Smith", "ws@domain.com", "I am an actor and a musician", 34}, {"Ala Nowak", "ala@nowak.com", "c-body", 35}};
    }

    public static String generateStrFromRsrc(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}
