package client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.http.ContentType.XML;

public class OSMConfigClient {

    private static final String BASE_URL = readProperty("apiUrl");
    public static String ACCESS_TOKEN = readProperty("accessToken");

    protected static RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .addFilter(new AllureRestAssured())
                .setContentType(XML)
                .setBaseUri(BASE_URL)
                .build().header("Authorization", "Bearer " + ACCESS_TOKEN);
    }

    // This method can be improved by using Map as input/output value (In this task it is unnecessary)
    private static String readProperty(String name) {
        Properties properties = new Properties();

        try (FileReader file = new FileReader("config.properties")) {
            properties.load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(name);
    }
}
