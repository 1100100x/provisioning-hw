package com.voverc.provisioning.service.converter;

import com.voverc.provisioning.service.dto.DeviceConfigurationDto;
import com.voverc.provisioning.service.dto.DeviceConfigurationOverrideDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PropsConfigurationDataConverter implements ConfigurationDataConverter {

    public String convert(DeviceConfigurationDto deviceConfigurationDto) {
        if (deviceConfigurationDto instanceof DeviceConfigurationOverrideDto) {
            return getPropertiesRepresentation(deviceConfigurationDto) + '\n' + "timeout=" + ((DeviceConfigurationOverrideDto) deviceConfigurationDto).getTimeout();
        }
        return getPropertiesRepresentation(deviceConfigurationDto);
    }

    private String getPropertiesRepresentation(DeviceConfigurationDto deviceConfigurationDto) {
        return "username=" + deviceConfigurationDto.getUsername() + '\n' +
                "password=" + deviceConfigurationDto.getPassword() + '\n' +
                "domain=" + deviceConfigurationDto.getDomain() + '\n' +
                "port=" + deviceConfigurationDto.getPort() + '\n' +
                "codecs=" + deviceConfigurationDto.getCodecs().toString();
    }

}
