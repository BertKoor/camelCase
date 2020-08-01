package nl.ordina.bertkoor.camelcase.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CHSResponseTest {

    @Test
    void testUnmarshal() throws JsonProcessingException {
        String json = "{\"phs\":7}";
        ObjectMapper mapper = new ObjectMapper();
        CHSResponse result = mapper.readValue(json, CHSResponse.class);
        assertEquals(7, result.getPhs());
    }
}