package nl.ordina.bertkoor.camelcase.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonUnmarshalTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testChsResponse_phs7() throws JsonProcessingException {
        String json = "{\"phs\":7}";
        CHSResponse result = mapper.readValue(json, CHSResponse.class);
        assertEquals(7, result.getPhs());
    }

    @Test
    void testCcrsResponse_noScore() throws JsonProcessingException {
        String json = "{}";
        CCRSResponse result = mapper.readValue(json, CCRSResponse.class);
        assertFalse(result.hasRating());
    }

    @Test
    void testCcrsResponse_rating3() throws JsonProcessingException {
        String json = "{\"rating\":3}";
        CCRSResponse result = mapper.readValue(json, CCRSResponse.class);
        assertTrue(result.hasRating());
        assertEquals(3, result.getRating());
    }
}
