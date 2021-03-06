public class PayloadRSA {
    public static String addPlace() {
        return "{\n" +
                "  \"location\": {\n" +
                "    \"lat\": -38.383494,\n" +
                "    \"lng\": 33.427362\n" +
                "  },\n" +
                "  \"accuracy\": 50,\n" +
                "  \"name\": \"123 name\",\n" +
                "  \"phone_number\": \"+48 983 893 3937\",\n" +
                "  \"address\": \"123 sample address\",\n" +
                "  \"types\": [\n" +
                "    \"shoe park\",\n" +
                "    \"shop\"\n" +
                "  ],\n" +
                "  \"website\": \"http://google.com\",\n" +
                "  \"language\": \"Polish\"\n" +
                "}";
    }

    public static String coursePrice() {
        return "{\n" +
                "\"dashboard\": {\n" +
                "\"purchaseAmount\": 910,\n" +
                "\"website\": \"rahulshettyacademy.com\"\n" +
                "},\n" +
                "\"courses\": [\n" +
                "{\n" +
                "\"title\": \"Selenium Python\",\n" +
                "\"price\": 50,\n" +
                "\"copies\": 6\n" +
                "},\n" +
                "{\n" +
                "\"title\": \"Cypress\",\n" +
                "\"price\": 40,\n" +
                "\"copies\": 4\n" +
                "},\n" +
                "{\n" +
                "\"title\": \"RPA\",\n" +
                "\"price\": 45,\n" +
                "\"copies\": 10\n" +
                "}\n" +
                "]\n" +
                "}";
    }

    public static String addBook() {
        return "{\n" +
                "    \"name\": \"LearnEEEE Appium Automation with Java\",\n" +
                "    \"isbn\": \"bczaaefd\",\n" +
                "    \"aisle\": \"227\",\n" +
                "    \"author\": \"Johny foe\"\n" +
                "}";
    }

    public static String courses() {
        return "{\n" +
                "    \"instructor\": \"RahulShetty\",\n" +
                "    \"url\": \"rahulshettycademy.com\",\n" +
                "    \"services\": \"projectSupport\",\n" +
                "    \"expertise\": \"Automation\",\n" +
                "    \"courses\": {\n" +
                "        \"webAutomation\": [\n" +
                "            {\n" +
                "                \"courseTitle\": \"Selenium Webdriver Java\",\n" +
                "                \"price\": \"50\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"courseTitle\": \"Cypress\",\n" +
                "                \"price\": \"40\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"courseTitle\": \"Protractor\",\n" +
                "                \"price\": \"40\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"api\": [\n" +
                "            {\n" +
                "                \"courseTitle\": \"Rest Assured Automation using Java\",\n" +
                "                \"price\": \"50\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"courseTitle\": \"SoapUI Webservices testing\",\n" +
                "                \"price\": \"40\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"mobile\": [\n" +
                "            {\n" +
                "                \"courseTitle\": \"Appium-Mobile Automation using Java\",\n" +
                "                \"price\": \"50\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"linkedIn\": \"https://www.linkedin.com/in/rahul-shetty-trainer/\"\n" +
                "}";

    }


}
