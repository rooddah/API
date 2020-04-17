import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.swing.*;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class JIRATests {
    /**
     * to install Jira locally --> https://www.atlassian.com/software/jira/download
     */

    public String issueKey;
    public String defaultKey = "TP-24";
    public SessionFilter session;

    @BeforeTest
    public void setUp() {
        RestAssured.baseURI = "http://localhost:8080";
        session = new SessionFilter();

        given().log().all().contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"username\": \"rooddah\",\n" +
                        "    \"password\": \"jira123\"\n" +
                        "}")
                .filter(session)
                .when().post("/rest/auth/1/session")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
        if (issueKey == null) {
            issueKey = defaultKey;
        }
    }

    @Test
    public void tc01_create_an_issue() {
        String key = "TP";
        String summary = "[Bug] some lonely bug";
        String description = "description";

        String response = given().log().all().contentType(ContentType.JSON)
                .body(PayloadJIRA.addIssue(key, summary, description))
                .filter(session)
                .when().post("/rest/api/2/issue")
                .then().log().all().assertThat().statusCode(201).extract().response().asString();

        JsonPath js = new JsonPath(response);
        issueKey = js.getString("key");

        System.out.println("the newly created key is: " + issueKey);
    }

    @Test
    public void tc02_add_comment_to_task() {
        String comment = "Some random comment";
        given().pathParam("key", issueKey).log().all().contentType(ContentType.JSON)
                .body(PayloadJIRA.addComment(comment))
                .filter(session)
                .when().post("rest/api/2/issue/{key}/comment")
                .then().log().all().assertThat().statusCode(201);
    }

    /**
     * This method is to ease one's life when it comes to bulk deletion of the issues
     */
    @Test
    public void tc03_delete_issues_1_to_10() {
        //  Values '1-10' are hardcoded to remove only those particular items. It is only to ease my work (when created multiple items and want to get rid of them) + to show code logic.
        //  This test cannot work on its own (my bad, I know [sic!]) when there are no 'TP-1 - TP-10' available. To be continued...

        String keyStr;
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            list.add("TP-" + i);
        }
        System.out.println("The following issues will be deleted: " + list);

        for (int i = 0; i < list.size(); i++) {
            keyStr = list.get(i);
            System.out.println("key is " + keyStr);
            given().log().all()
                    .filter(session)
                    .when().delete("/rest/api/2/issue/"+keyStr+"")
                    .then().log().all().assertThat().statusCode(204);
        }
    }

    @Test
    public void tc04_add_attachment() {
        given().pathParam("key", issueKey).log().all().header("X-Atlassian-Token", "no-check").header("Content-Type", "multipart/form-data")
                .multiPart("file", new File( "D:/automation/resources/myfile.txt"))
                .formParam("description", "This is my txt attachment")
                .filter(session)
                .when().post("/rest/api/2/issue/{key}/attachments")
                .then().log().all().assertThat().statusCode(200);
    }

    @Test
    public void tc05_get_issue() {
        String issueDetails = given().pathParam("key", issueKey).log().all()
                .queryParam("fields", "summary, creator, priority, status")
                .filter(session)
                .when().get("/rest/api/2/issue/{key}")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();

    }
}
