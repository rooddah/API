import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Rsa {
    RequestSpecification req;

    @BeforeTest
    public void setUp() {

        String url = "https://rahulshettyacademy.com";
        req = new RequestSpecBuilder()
                .setBaseUri(url)
                .addQueryParam("key", "qaclick123")
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void tc01_post_new_entry() {
        given().spec(req)
                .body(PayloadRSA.addPlace())
                .when().post("maps/api/place/add/json")
                .then().assertThat().statusCode(200);
    }

    @Test
    public void tc02_post_update_and_check_place() {
        String newAddress = "Even newer address, USA";
        String response = given().log().all().spec(req)
                .body(PayloadRSA.addPlace())
                .when().post("maps/api/place/add/json")
                .then().assertThat().statusCode(200).body("scope", equalTo("APP")).extract().response().asString();

        JsonPath jsonPath = new JsonPath(response);
        String placeId = jsonPath.getString("place_id");
        System.out.println("Place ID is: " + placeId);

        //update the place
        given().log().all().spec(req)
                .body("{\n" +
                        "\"place_id\":\"" + placeId + "\",\n" +
                        "\"address\":\"" + newAddress + "\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}")
                .when().put("/maps/api/place/update/json")
                .then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));

        //checking the place
        String getPlaceResponse = given().log().all().spec(req).queryParam("place_id", placeId)
                .when().get("maps/api/place/get/json")
                .then().assertThat().log().all().statusCode(200).extract().response().asString();

        JsonPath js = new JsonPath(getPlaceResponse);
        String actualAddress = js.getString("address");
        Assert.assertEquals(actualAddress, newAddress);
    }

    @Test
    public void tc03_getCertainData() {
        JsonPath js = new JsonPath(PayloadRSA.coursePrice());   //in case we need some mock data
        //print no. of courses
        int number = js.getList("courses").size();
        //get purchase amount
        int purchAm = js.getInt("dashboard.purchaseAmount");
        //title of the first course
        String firstCourse = js.getList("courses.title").get(0).toString();
        String secondCourse = js.get("courses[1].title").toString();

        System.out.println("Number of courses: " + number);
        System.out.println("The total purchase amount is: " + purchAm);
        System.out.println("first course is: " + firstCourse + " and second one is: " + secondCourse);

        //sum of the courses' price
        List<Integer> list = js.getList("courses.price");
        int sum = list.stream().mapToInt(Integer::intValue).sum();
        System.out.println("The sum of all courses price is: " + sum);

        //get title and price for each course
        System.out.println("Course titles:");
        for (int i = 0; i < number; i++) {
            String title = js.get("courses[" + i + "].title");
            int price = js.get("courses[" + i + "].price");
            System.out.println(i + 1 + ". " + title + " and its price is " + price);
        }
    }

    @Test
    public void tc04() {
        RestAssured.baseURI = "http://216.10.245.166";
        String resp = given().contentType(ContentType.JSON)
                .body(PayloadRSA.addBook())
                .when().post("/Library/Addbook.php")
                .then().assertThat().statusCode(200).extract().response().asString();
        JsonPath js = new JsonPath(resp);
        String id = js.get("ID");
        System.out.println("Id is " + id);
    }
}
