package com.voverc.provisioning.service.converter;

import com.voverc.provisioning.service.dto.DeviceConfigurationDto;
import com.voverc.provisioning.service.dto.DeviceConfigurationOverrideDto;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.voverc.provisioning.service.util.TestUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PropsConfigurationDataConverterTest {

    private final PropsConfigurationDataConverter propsConfigurationDataConverter = new PropsConfigurationDataConverter();

    @Test
    void convertToPropsWithOverride() {
        DeviceConfigurationOverrideDto deviceConfigurationOverrideDto = new DeviceConfigurationOverrideDto(
                USERNAME,
                PSWRD,
                DOMAIN,
                PORT,
                Arrays.asList("G711", "G729", "OPUS"),
                10
        );

        String expected = "username=john\n" +
                "password=doe\n" +
                "domain=sip.anotherdomain.com\n" +
                "port=5161\n" +
                "codecs=[G711, G729, OPUS]\n" +
                "timeout=10";

        String actual = propsConfigurationDataConverter.convert(deviceConfigurationOverrideDto);
        assertEquals(expected, actual);
    }

    @Test
    void convertToPropsWithoutOverride() {
        DeviceConfigurationDto deviceConfigurationDto = new DeviceConfigurationDto(
                USERNAME,
                PSWRD,
                "sip.voverc.com",
                5060,
                Arrays.asList("G711", "G729", "OPUS")
        );

        String expected = "username=john\n" +
                "password=doe\n" +
                "domain=sip.voverc.com\n" +
                "port=5060\n" +
                "codecs=[G711, G729, OPUS]";
        String actual = propsConfigurationDataConverter.convert(deviceConfigurationDto);
        assertEquals(expected, actual);
    }
}