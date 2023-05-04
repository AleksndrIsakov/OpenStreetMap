import client.OSMRestClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import generator.PrefGenerator;
import io.qameta.allure.Story;
import io.restassured.response.ValidatableResponse;
import model.OSM;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Story("Delete preferences")
public class UserDeletePreferencesTest {

    private OSMRestClient client;
    private OSM osm;
    private String key;

    @Before
    public void setUp() throws JsonProcessingException {
        client = new OSMRestClient();
        osm = new OSM();
        key = "key_to_delete";
        osm.setVersion("0.6");
        osm.setGenerator("OpenStreetMap server");
        osm.setPreference(PrefGenerator.list(key, RandomStringUtils.randomAlphabetic(10), 1,false));
        client.addPreferences(new XmlMapper().writeValueAsString(osm));
    }

    @After
    public void tearDown() {
        client.deletePreferences(key);
    }

    @Test
    @DisplayName("Check successful deletion existed preference")
    public void checkDeletePreference() {
        ValidatableResponse response = client.deletePreferences(key);
        int statusCode = response.extract().statusCode();
        assertThat(statusCode, equalTo(SC_OK));
    }

    @Test
    @DisplayName("Check deletion of non existed preference")
    public void checkDeletionOfNonExistentPreference() {
        ValidatableResponse response = client.deletePreferences("not_exist");

        int statusCode = response.extract().statusCode();
        assertThat(statusCode, equalTo(SC_NOT_FOUND));
    }
}
