package com.voverc.provisioning.service.factory;

import com.voverc.provisioning.entity.Device;
import com.voverc.provisioning.properties.ProvisioningProperties;
import com.voverc.provisioning.service.dto.DeviceConfigurationDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static com.voverc.provisioning.service.util.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceConfigurationDtoFactoryTest {

    @Mock
    private ProvisioningProperties provisioningProperties;

    @InjectMocks
    private DeviceConfigurationDtoFactory deviceConfigurationDtoFactory;

    @Test
    void getDeviceConfigurationDto() {
        when(provisioningProperties.getDomain()).thenReturn(DOMAIN);
        when(provisioningProperties.getPort()).thenReturn(PORT);
        when(provisioningProperties.getCodecs()).thenReturn(Arrays.asList("G711", "G729", "OPUS"));

        DeviceConfigurationDto expected = new DeviceConfigurationDto(
                USERNAME,
                PSWRD,
                DOMAIN,
                PORT,
                Arrays.asList("G711", "G729", "OPUS")
        );

        Device device = mock(Device.class);

        when(device.getUsername()).thenReturn(USERNAME);
        when(device.getPassword()).thenReturn(PSWRD);


        DeviceConfigurationDto actual = deviceConfigurationDtoFactory.getDeviceConfigurationDto(device);
        assertThat(expected).isEqualToComparingFieldByField(actual);
    }
}