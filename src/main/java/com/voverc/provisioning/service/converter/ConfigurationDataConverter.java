package com.voverc.provisioning.service.converter;

import com.voverc.provisioning.service.dto.DeviceConfigurationDto;

public interface ConfigurationDataConverter {
    String convert(DeviceConfigurationDto deviceConfigurationDto);
}
