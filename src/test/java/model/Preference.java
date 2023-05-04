package model;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "preference")
public class Preference {
        @JacksonXmlProperty(isAttribute = true)
        private String k;
        @JacksonXmlProperty(isAttribute = true)
        private String v;
}


