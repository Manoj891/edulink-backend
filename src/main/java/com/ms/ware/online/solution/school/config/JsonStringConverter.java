package com.ms.ware.online.solution.school.config;


import com.ms.ware.online.solution.school.exception.CustomException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JsonStringConverter {
    @Getter
    private final ObjectMapper mapper = new ObjectMapper();
    private final JsonFactory factory = mapper.getFactory();

    public JsonNode toJsonNode(String jsonData) {
        try {
            JsonParser jp = factory.createParser(jsonData);
            return mapper.readTree(jp);
        } catch (Exception e) {
            log.info(e.getMessage() + " \n" + jsonData);
        }
        return null;
    }

    public String jsonString(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }
}
