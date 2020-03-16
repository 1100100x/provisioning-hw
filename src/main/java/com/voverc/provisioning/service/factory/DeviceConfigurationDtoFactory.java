package com.voverc.provisioning.service.factory;

import com.voverc.provisioning.entity.Device;
import com.voverc.provisioning.properties.ProvisioningProperties;
import com.voverc.provisioning.service.dto.DeviceConfigurationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeviceConfigurationDtoFactory implements DtoFactory<DeviceConfigurationDto> {

    private final ProvisioningProperties provisioningProperties;

    @Override
    public DeviceConfigurationDto getDeviceConfigurationDto(Device device) {
        return new DeviceConfigurationDto(
                device.getUsername(),
                device.getPassword(),
                provisioningProperties.getDomain(),
                provisioningProperties.getPort(),
                provisioningProperties.getCodecs());
    }
}
