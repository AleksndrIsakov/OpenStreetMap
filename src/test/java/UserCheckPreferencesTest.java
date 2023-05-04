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

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

@Story("Preferences response")
public class UserCheckPreferencesTest {

    private OSMRestClient client;
    private OSM osm;

    @Before
    public void setUp() throws JsonProcessingException {
        client = new OSMRestClient();
        osm = new OSM();
        osm.setVersion("0.6");
        osm.setGenerator("OpenStreetMap server");
        osm.setPreference(PrefGenerator.list("check", RandomStringUtils.randomAlphabetic(3), 5,true));
        client.addPreferences(new XmlMapper().writeValueAsString(osm));
    }

    @Test
    @DisplayName("Check return values in preferences response")
    public void checkPreferences() throws JsonProcessingException {
        ValidatableResponse response = client.getPreferences();
        int statusCode = response.extract().statusCode();
        assertThat(statusCode, equalTo(SC_OK));

        String xml = response.extract().body().asString();
        XmlMapper mapper = new XmlMapper();
        OSM actual = mapper.readValue(response.extract().asString(), OSM.class);

        assertThat(osm.getPreference(), is(actual.getPreference()));
    }

    @After
    public void tearDown() {
        osm.getPreference().stream().forEach(x->client.deletePreferences(x.getK()));
    }

}
