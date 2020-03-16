package com.voverc.provisioning.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voverc.provisioning.domain.DeviceModel;
import com.voverc.provisioning.entity.Device;
import com.voverc.provisioning.exception.DeviceNotFoundException;
import com.voverc.provisioning.repository.DeviceRepository;
import com.voverc.provisioning.service.converter.ConfigurationDataConverter;
import com.voverc.provisioning.service.converter.JsonConfigurationDataConverter;
import com.voverc.provisioning.service.converter.PropsConfigurationDataConverter;
import com.voverc.provisioning.service.dto.DeviceConfigurationDto;
import com.voverc.provisioning.service.dto.DeviceConfigurationOverrideDto;
import com.voverc.provisioning.service.factory.DeviceConfigurationDtoFactory;
import com.voverc.provisioning.service.factory.DeviceConfigurationOverrideDtoFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import static com.voverc.provisioning.service.util.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProvisioningServiceImplTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private DeviceConfigurationDtoFactory deviceConfigurationDtoFactory;

    @Mock
    private DeviceConfigurationOverrideDtoFactory deviceConfigurationOverrideDtoFactory;

    @Spy
    private Map<DeviceModel, ConfigurationDataConverter> deviceModelToDataConverter = new EnumMap<DeviceModel, ConfigurationDataConverter>(DeviceModel.class) {{
        put(DeviceModel.CONFERENCE, new JsonConfigurationDataConverter(new ObjectMapper()));
        put(DeviceModel.DESK, new PropsConfigurationDataConverter());
    }};

    @InjectMocks
    private ProvisioningServiceImpl provisioningService;

    private static final String MAC_ADDRESS = "m-a-c-a-d-r-e-s-s";

    @Test
    void getProvisioningPropFileWithoutOverrideSuccessfully() {

        Device device = new Device();
        device.setMacAddress(MAC_ADDRESS);
        device.setModel(DeviceModel.DESK);
        device.setPassword(PSWRD);
        device.setUsername(USERNAME);

        DeviceConfigurationDto deviceConfigurationDto =
        new DeviceConfigurationDto(
                device.getUsername(),
                device.getPassword(),
                "sip.voverc.com",
                5060,
                Arrays.asList("G711", "G729", "OPUS"));

        when(deviceRepository.findById(MAC_ADDRESS)).thenReturn(Optional.of(device));
        when(deviceConfigurationDtoFactory.getDeviceConfigurationDto(device)).thenReturn(deviceConfigurationDto);

        String expected = "username=john\n" +
                "password=doe\n" +
                "domain=sip.voverc.com\n" +
                "port=5060\n" +
                "codecs=[G711, G729, OPUS]";
        String actual = provisioningService.getProvisioningFile(MAC_ADDRESS);

        assertEquals(expected, actual);

        verifyZeroInteractions(deviceConfigurationOverrideDtoFactory);
    }

    @Test
    void getProvisioningJsonFileWithOverrideSuccessfully() {
        Device device = new Device();
        device.setMacAddress(MAC_ADDRESS);
        device.setModel(DeviceModel.DESK);
        device.setOverrideFragment("domain=sip.anotherdomain.com\n" +
                "port=5161\n" +
                "timeout=10");
        device.setPassword(PSWRD);
        device.setUsername(USERNAME);

        DeviceConfigurationOverrideDto deviceConfigurationDto =
                new DeviceConfigurationOverrideDto(
                        device.getUsername(),
                        device.getPassword(),
                        DOMAIN,
                        PORT,
                        Arrays.asList("G711", "G729", "OPUS"),
                        10);

        when(deviceRepository.findById(MAC_ADDRESS)).thenReturn(Optional.of(device));
        when(deviceConfigurationOverrideDtoFactory.getDeviceConfigurationDto(device)).thenReturn(deviceConfigurationDto);

        String expected = "username=john\n" +
                "password=doe\n" +
                "domain=sip.anotherdomain.com\n" +
                "port=5161\n" +
                "codecs=[G711, G729, OPUS]\n" +
                "timeout=10";

        String actual = provisioningService.getProvisioningFile(MAC_ADDRESS);

        assertEquals(expected, actual);

        verifyZeroInteractions(deviceConfigurationDtoFactory);
    }

    @Test
    void getProvisioningFileThrowsException() {
        when(deviceRepository.findById(MAC_ADDRESS)).thenReturn(Optional.empty());

        assertThatExceptionOfType(DeviceNotFoundException.class)
                .isThrownBy(() -> provisioningService.getProvisioningFile(MAC_ADDRESS))
                .withMessage(String.format("device with mac address '%s' not found ", MAC_ADDRESS));
    }
}