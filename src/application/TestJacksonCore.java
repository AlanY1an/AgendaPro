package application;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.StringWriter;

public class TestJacksonCore {
    public static void main(String[] args) {
        try {
            JsonFactory factory = new JsonFactory();
            StringWriter writer = new StringWriter();
            JsonGenerator generator = factory.createGenerator(writer);
            generator.writeStartObject();
            generator.writeStringField("message", "Jackson Core is working!");
            generator.writeEndObject();
            generator.close();
            System.out.println(writer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
