import java.util.Random;

public class PayloadLocal {
    public static String addComment() {
        return addComment(null, null, null, 0);
    }

    public static String addComment(String name, String email, String body, int postId) {
        Random rnd = new Random();
        if (name == null) {
            name = "John Doe";
        }
        if (email == null) {
            email = "john.doe@domain.com";
        }
        if (body == null) {
            body = "This is a random, boring, usual body";
        }
        if (postId == 0) {
            postId = rnd.nextInt(100);
        }
        return "{\n" +
                "  \"name\": \""+name+"\",\n" +
                "  \"email\": \""+email+"\",\n" +
                "  \"body\": \""+body+"\",\n" +
                "  \"postId\": "+postId+"\n" +
                "}";
    }
}
