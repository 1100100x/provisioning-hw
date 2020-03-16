package com.voverc.provisioning.service.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voverc.provisioning.domain.DeviceModel;
import com.voverc.provisioning.entity.Device;
import com.voverc.provisioning.exception.DataConversionException;
import com.voverc.provisioning.properties.ProvisioningProperties;
import com.voverc.provisioning.service.dto.DeviceConfigurationOverrideDto;
import com.voverc.provisioning.service.dto.OverrideFragmentDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static com.voverc.provisioning.service.util.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceConfigurationOverrideDtoFactoryTest {

    @Spy
    private ObjectMapper objectMapper;

    @Mock
    private ProvisioningProperties provisioningProperties;

    @InjectMocks
    private DeviceConfigurationOverrideDtoFactory deviceConfigurationOverrideDtoFactory;

    @Test
    void deviceConfigurationDtoFromPropsOverride() {
        Device device = new Device();
        device.setMacAddress("aa-bb-cc-11-22-33");
        device.setModel(DeviceModel.DESK);
        device.setOverrideFragment("domain=sip.anotherdomain.com\n" +
                "port=5161\n" +
                "timeout=10");
        device.setPassword(PSWRD);
        device.setUsername(USERNAME);

        when(provisioningProperties.getCodecs()).thenReturn(Arrays.asList("G711", "G729", "OPUS"));

        DeviceConfigurationOverrideDto expected = new DeviceConfigurationOverrideDto(
                USERNAME,
                PSWRD,
                DOMAIN,
                PORT,
                Arrays.asList("G711", "G729", "OPUS"),
                10
        );

        DeviceConfigurationOverrideDto actual = deviceConfigurationOverrideDtoFactory.getDeviceConfigurationDto(device);
        assertThat(expected).isEqualToComparingFieldByField(actual);

    }

    @Test
    void deviceConfigurationDtoFromJsonOverride() {
        Device device = new Device();
        device.setMacAddress("aa-bb-cc-11-22-32");
        device.setModel(DeviceModel.CONFERENCE);
        device.setOverrideFragment("{" +
                "\"domain\":\"sip.anotherdomain.com\"," +
                "\"port\":\"5161\"," +
                "\"timeout\":10" +
                "}");
        device.setPassword(PSWRD);
        device.setUsername(USERNAME);

        when(provisioningProperties.getCodecs()).thenReturn(Arrays.asList("G711", "G729", "OPUS"));

        DeviceConfigurationOverrideDto expected = new DeviceConfigurationOverrideDto(
                USERNAME,
                PSWRD,
                DOMAIN,
                PORT,
                Arrays.asList("G711", "G729", "OPUS"),
                10
        );

        DeviceConfigurationOverrideDto actual = deviceConfigurationOverrideDtoFactory.getDeviceConfigurationDto(device);
        assertThat(expected).isEqualToComparingFieldByField(actual);
    }

    @Test
    void readJsonThrowsDataConversionException() throws Exception {
        Device device = new Device();
        device.setMacAddress("aa-bb-cc-11-22-32");
        device.setModel(DeviceModel.CONFERENCE);
        String overrideFragment = "{}";
        device.setOverrideFragment(overrideFragment);
        device.setPassword(PSWRD);
        device.setUsername(USERNAME);

        when(objectMapper.readValue(overrideFragment, OverrideFragmentDto.class)).thenThrow(JsonProcessingException.class);
        assertThatExceptionOfType(DataConversionException.class)
                .isThrownBy(() -> deviceConfigurationOverrideDtoFactory.getDeviceConfigurationDto(device))
                .withNoCause();
    }

    @Test
    void readPropsThrowsDataConversionException() {
        Device device = new Device();
        device.setMacAddress("aa-bb-cc-11-22-33");
        device.setModel(DeviceModel.DESK);
        device.setOverrideFragment(null);
        device.setPassword(PSWRD);
        device.setUsername(USERNAME);

        assertThatExceptionOfType(DataConversionException.class)
                .isThrownBy(() -> deviceConfigurationOverrideDtoFactory.getDeviceConfigurationDto(device))
                .withNoCause();
    }
}