package model;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "osm")
public class OSM {
    @JacksonXmlProperty(isAttribute = true)
    private String version;
    @JacksonXmlProperty(isAttribute = true)
    private String generator;
    @JacksonXmlProperty(isAttribute = true)
    private String copyright;
    @JacksonXmlProperty(isAttribute = true)
    private String attribution;
    @JacksonXmlProperty(isAttribute = true)
    private String license;
    @JacksonXmlElementWrapper(localName = "permissions")
    private List<Permission> permissions;
    @JacksonXmlElementWrapper(localName = "preferences")
    private List<Preference> preference;
}
