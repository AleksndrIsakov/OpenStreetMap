import client.OSMConfigClient;
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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Story("Create preferences")
@RunWith(Parameterized.class)
public class UserCreatePreferencesTest extends OSMConfigClient {

    private OSMRestClient client;
    private List<String> keys;
    private OSM osm;
    private int status;

    @Before
    public void setUp() {
        client = new OSMRestClient();
    }

    @After
    public void tearDown() {
        for (String key: keys)
            client.deletePreferences(key);
    }

    public UserCreatePreferencesTest(String key, String value, int count, int expectedCode, boolean isUnique) {
        osm = new OSM();
        keys = new ArrayList<>();
        osm.setVersion("0.6");
        osm.setGenerator("OpenStreetMap server");
        osm.setPreference(PrefGenerator.list(key, value, count, isUnique));
        status = expectedCode;
    }

    @Parameterized.Parameters
    public static Object[][] setData() {
        return new Object[][]{
                {"any_key", "any_value", 1, SC_OK, true},
                {"any_key", "any_value", 3, SC_NOT_ACCEPTABLE, false}, // must return HTTP response code 406 (not acceptable) if the same key occurs more than once
                {RandomStringUtils.randomAlphabetic(256), "any_value", 1, SC_BAD_REQUEST, false}, // The sizes of the key and value are limited to 255 characters.
                {"any_key", RandomStringUtils.randomAlphabetic(256), 1, SC_BAD_REQUEST, false},   // The sizes of the key and value are limited to 255 characters.
                {"any_key", "any_value", 151, SC_REQUEST_TOO_LONG, true} // must return HTTP response code 413 (request entity too large) if you try to upload more than 150 preferences at once
        };
    }

    @Test
    @DisplayName("Check preferences creation")
    public void checkAddPreferences() throws JsonProcessingException {
        String xml = new XmlMapper().writeValueAsString(osm);
        ValidatableResponse response = client.addPreferences(xml);

        int statusCode = response.extract().statusCode();
        assertThat(statusCode, equalTo(status));

        // need to erase created data
        if (statusCode == SC_OK)
            keys = osm.getPreference().stream().map(x->x.getK()).collect(Collectors.toList());
    }
}
