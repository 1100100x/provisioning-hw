package com.voverc.provisioning.service.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voverc.provisioning.exception.DataConversionException;
import com.voverc.provisioning.service.dto.DeviceConfigurationDto;
import com.voverc.provisioning.service.dto.DeviceConfigurationOverrideDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static com.voverc.provisioning.service.util.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JsonConfigurationDataConverterTest {

    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private JsonConfigurationDataConverter jsonConfigurationDataConverter;

    @Test
    void convertToJsonWithOverride() {
        DeviceConfigurationOverrideDto deviceConfigurationOverrideDto = new DeviceConfigurationOverrideDto(
                USERNAME,
                PSWRD,
                DOMAIN,
                PORT,
                Arrays.asList("G711", "G729", "OPUS"),
                10
        );

        String expected = "{" +
                "\"username\":\"john\"," +
                "\"password\":\"doe\"," +
                "\"domain\":\"sip.anotherdomain.com\"," +
                "\"port\":5161," +
                "\"codecs\":[\"G711\",\"G729\",\"OPUS\"]," +
                "\"timeout\":10" +
                "}";

        String actual = jsonConfigurationDataConverter.convert(deviceConfigurationOverrideDto);
        assertEquals(expected, actual);

    }

    @Test
    void convertToJsonWithoutOverride() {
        DeviceConfigurationDto deviceConfigurationDto = new DeviceConfigurationDto(
                USERNAME,
                PSWRD,
                "sip.voverc.com",
                5060,
                Arrays.asList("G711", "G729", "OPUS")
        );

        String expected = "{" +
                "\"username\":\"john\"," +
                "\"password\":\"doe\"," +
                "\"domain\":\"sip.voverc.com\"," +
                "\"port\":5060," +
                "\"codecs\":[\"G711\",\"G729\",\"OPUS\"]" +
                "}";

        String actual = jsonConfigurationDataConverter.convert(deviceConfigurationDto);
        assertEquals(expected, actual);

    }

    @Test
    void convertToJsonThrowsDataConversionException() throws Exception {
        when(objectMapper.writeValueAsString(any(DeviceConfigurationDto.class))).thenThrow(JsonProcessingException.class);
        assertThatExceptionOfType(DataConversionException.class)
                .isThrownBy(() -> jsonConfigurationDataConverter.convert(mock(DeviceConfigurationDto.class)))
                .withNoCause();
    }
}