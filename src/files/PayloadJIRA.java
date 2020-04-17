public class PayloadJIRA {

    public static String addIssue(String key, String summary, String description) {
        return "{\n" +
                "\t\"fields\":\n" +
                "\t{\n" +
                "\t\t\"project\":\n" +
                "\t\t{\n" +
                "\t\t\t\"key\": \""+key+"\"\n" +
                "\t\t},\n" +
                "\t\t\"summary\": \""+summary+"\",\n" +
                "\t\t\"description\": \""+description+"\",\n" +
                "\t\t\"issuetype\": \n" +
                "\t\t{\n" +
                "\t\t\t\"name\": \"Bug\"\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";
    }

    public static String addComment(String body) {
        return "{\n" +
                "    \"visibility\": {\n" +
                "        \"type\": \"role\",\n" +
                "        \"value\": \"Administrators\"\n" +
                "    },\n" +
                "    \"body\": \""+body+"\"\n" +
                "}";
    }

    public enum IssueType {
        SUB_TASK("Sub-task"),
        BUG("Bug");

        private final String s;

        IssueType(String s) {
            this.s = s;
        }

        public String toString() {
            return this.s;
        }
    }
}
