package dev.sobue.ai.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** Verifies normal JSON parsing and early rejection of oversized property names in both Jackson generations. */
class JsonParsingTests {

    @Test
    @DisplayName("Jackson 2 preserves ordinary OpenAI response JSON")
    void jackson2ParsesResponse() throws Exception {
        var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        var response = mapper.readTree("{\"choices\":[{\"message\":{\"content\":\"hello\"}}]}");
        assertEquals("hello", response.path("choices").get(0).path("message").path("content").asText());
    }

    @Test
    @DisplayName("Jackson 3 preserves ordinary OpenAI response JSON")
    void jackson3ParsesResponse() {
        var mapper = tools.jackson.databind.json.JsonMapper.builder().build();
        var response = mapper.readTree("{\"choices\":[{\"message\":{\"content\":\"hello\"}}]}");
        assertEquals("hello", response.path("choices").get(0).path("message").path("content").asText());
    }

    @ParameterizedTest
    @ValueSource(strings = {"x", "\\u0078"})
    @DisplayName("Jackson 2 rejects oversized names before consuming the entire untrusted Reader")
    void jackson2RejectsOversizedNameEarly(String nameUnit) throws Exception {
        var constraints = com.fasterxml.jackson.core.StreamReadConstraints.builder().maxNameLength(64).build();
        var factory = com.fasterxml.jackson.core.JsonFactory.builder().streamReadConstraints(constraints).build();
        var mapper = new com.fasterxml.jackson.databind.ObjectMapper(factory);
        try (var input = oversizedName(nameUnit)) {
            assertThrows(com.fasterxml.jackson.core.exc.StreamConstraintsException.class, () -> mapper.readTree(input));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"x", "\\u0078"})
    @DisplayName("Jackson 3 rejects oversized names before consuming the entire untrusted Reader")
    void jackson3RejectsOversizedNameEarly(String nameUnit) throws Exception {
        var constraints = tools.jackson.core.StreamReadConstraints.builder().maxNameLength(64).build();
        var factory = tools.jackson.core.json.JsonFactory.builder().streamReadConstraints(constraints).build();
        var mapper = tools.jackson.databind.json.JsonMapper.builder(factory).build();
        try (var input = oversizedName(nameUnit)) {
            assertThrows(tools.jackson.core.exc.StreamConstraintsException.class, () -> mapper.readTree(input));
        }
    }

    private Reader oversizedName(String nameUnit) {
        return new StringReader("{\"" + nameUnit.repeat(65536) + "\":0}") {
            private int consumed;

            @Override
            public int read(char[] buffer, int offset, int length) throws IOException {
                int count = super.read(buffer, offset, Math.min(length, 1024));
                consumed += Math.max(count, 0);
                if (consumed > 8192) {
                    throw new IOException("Property name was not rejected within the read budget");
                }
                return count;
            }
        };
    }
}
