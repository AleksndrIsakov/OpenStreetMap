import client.OSMRestClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.qameta.allure.Story;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import model.OSM;
import io.qameta.allure.junit4.DisplayName;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

@Story("Check permissions")
public class UserCheckPermissionTest {

    private OSMRestClient client;

    @Before
    public void setUp() {
        client = new OSMRestClient();
    }

    @Test
    @DisplayName("Check default user permissions")
    // This test must be combined with UI or SQL query actions that change user permissions
    // This test checks that user permission didn't change only
    public void checkUserPermission() throws JsonProcessingException {
        ValidatableResponse response = client.getPermissions();
        int statusCode = response.extract().statusCode();
        assertThat(statusCode, equalTo(SC_OK));

        String xml = response.extract().body().asString();
        XmlMapper mapper = new XmlMapper();
        OSM user = mapper.readValue(response.extract().asString(), OSM.class);

        List<String> expected = Arrays.asList("allow_read_prefs", "allow_write_prefs", "allow_write_diary", "allow_write_api", "allow_read_gpx", "allow_write_gpx", "allow_write_notes");
        List<String> actual = user.getPermissions().stream().map(x->x.getName()).collect(Collectors.toList());

        assertThat(expected, is(actual));
    }

}
