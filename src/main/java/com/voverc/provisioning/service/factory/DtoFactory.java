package com.voverc.provisioning.service.factory;

import com.voverc.provisioning.entity.Device;
import com.voverc.provisioning.service.dto.DeviceConfigurationDto;

public interface DtoFactory<E extends DeviceConfigurationDto>{

    E getDeviceConfigurationDto(Device device);

}
