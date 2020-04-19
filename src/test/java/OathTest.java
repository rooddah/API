import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;
import pojoObj.RSA.Courses.Api;
import pojoObj.RSA.RsaGetCourse;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

public class OathTest {
    @Test
    public void tc01_get_courses() throws InterruptedException, IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream("parameter.properties"));
        String pass = prop.getProperty("password");
        String email = prop.getProperty("email");

        String authUrl = "https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email" +
                "&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com" +
                "&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php";
        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(authUrl);
        driver.findElement(By.cssSelector("input[type='email']")).sendKeys(email);
        driver.findElement(By.id("identifierNext")).click();
        Thread.sleep(3000);
        driver.findElement(By.cssSelector("input[type='password']")).sendKeys(pass);
        driver.findElement(By.id("passwordNext")).click();
        Thread.sleep(5000);
        String url = driver.getCurrentUrl();
        String url2 = url.split("code=")[1];
        String code = url2.split("&scope")[0];
        System.out.println("code is " + code);
        driver.close();

        String accessTokenResp = given().urlEncodingEnabled(false).queryParam("code", code).queryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .queryParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W").queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
                .queryParam("grant_type", "authorization_code")
                .when().log().all()
                .post("https://www.googleapis.com/oauth2/v4/token").asString();

        JsonPath js = new JsonPath(accessTokenResp);
        String accessToken = js.getString("access_token");
        System.out.println("access token is " + accessToken);

        RsaGetCourse getCourse = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
                .when().get("https://rahulshettyacademy.com/getCourse.php").as(RsaGetCourse.class);

        List<Api> apiCourses = getCourse.getCourses().getApi();
        for (int i = 0; i < apiCourses.size(); i++) {
            if (apiCourses.get(i).getCourseTitle().equals("Rest Assured Automation using Java")) {
                String price = apiCourses.get(i).getPrice();
                System.out.println("'Rest Assured Automation using Java' course costs " + price);
            }
        }
    }
}
