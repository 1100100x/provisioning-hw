package com.voverc.provisioning.service;

import com.voverc.provisioning.domain.DeviceModel;
import com.voverc.provisioning.entity.Device;
import com.voverc.provisioning.exception.DeviceNotFoundException;
import com.voverc.provisioning.repository.DeviceRepository;
import com.voverc.provisioning.service.converter.ConfigurationDataConverter;
import com.voverc.provisioning.service.dto.DeviceConfigurationDto;
import com.voverc.provisioning.service.factory.DeviceConfigurationDtoFactory;
import com.voverc.provisioning.service.factory.DeviceConfigurationOverrideDtoFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProvisioningServiceImpl implements ProvisioningService {

    private final DeviceRepository deviceRepository;

    private final DeviceConfigurationDtoFactory deviceConfigurationDtoFactory;

    private final DeviceConfigurationOverrideDtoFactory deviceConfigurationOverrideDtoFactory;

    private final Map<DeviceModel, ConfigurationDataConverter> deviceModelToDataConverter;

    @Override
    public String getProvisioningFile(String macAddress) {

        Device device = deviceRepository
                .findById(macAddress)
                .orElseThrow(() -> new DeviceNotFoundException(String.format("device with mac address '%s' not found ", macAddress)));

        DeviceConfigurationDto deviceConfigurationDto = Objects.isNull(device.getOverrideFragment())
                ? deviceConfigurationDtoFactory.getDeviceConfigurationDto(device)
                : deviceConfigurationOverrideDtoFactory.getDeviceConfigurationDto(device);

        return deviceModelToDataConverter.get(device.getModel()).convert(deviceConfigurationDto);
    }

}
