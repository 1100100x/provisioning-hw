package com.voverc.provisioning.service.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voverc.provisioning.domain.DeviceModel;
import com.voverc.provisioning.entity.Device;
import com.voverc.provisioning.exception.DataConversionException;
import com.voverc.provisioning.properties.ProvisioningProperties;
import com.voverc.provisioning.service.dto.DeviceConfigurationOverrideDto;
import com.voverc.provisioning.service.dto.OverrideFragmentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

@Component
public class DeviceConfigurationOverrideDtoFactory implements DtoFactory<DeviceConfigurationOverrideDto> {

    private static final String DOMAIN = "domain";
    private static final String PORT = "port";
    private static final String TIMEOUT = "timeout";

    private ObjectMapper objectMapper;

    private ProvisioningProperties provisioningProperties;

    private Map<DeviceModel, Function<Device, DeviceConfigurationOverrideDto>> deviceModelToDto;

    @Autowired
    public DeviceConfigurationOverrideDtoFactory(ObjectMapper objectMapper, ProvisioningProperties provisioningProperties) {
        this.objectMapper = objectMapper;
        this.provisioningProperties = provisioningProperties;
        deviceModelToDto = new EnumMap<>(DeviceModel.class);
        deviceModelToDto.put(DeviceModel.CONFERENCE, this::deviceConfigurationDtoFromJsonOverride);
        deviceModelToDto.put(DeviceModel.DESK, this::deviceConfigurationDtoFromPropsOverride);
    }

    public DeviceConfigurationOverrideDto getDeviceConfigurationDto(Device device) {
        return deviceModelToDto.get(device.getModel()).apply(device);
    }

    private DeviceConfigurationOverrideDto deviceConfigurationDtoFromPropsOverride(Device device) {
        Properties props = getProps(device.getOverrideFragment());
        return new DeviceConfigurationOverrideDto(
                device.getUsername(),
                device.getPassword(),
                props.getProperty(DOMAIN),
                Integer.valueOf(props.getProperty(PORT)),
                provisioningProperties.getCodecs(),
                Integer.valueOf(props.getProperty(TIMEOUT)));
    }

    private DeviceConfigurationOverrideDto deviceConfigurationDtoFromJsonOverride(Device device) {
        OverrideFragmentDto fragment = getOverrideFragment(device.getOverrideFragment());
        return new DeviceConfigurationOverrideDto(
                device.getUsername(),
                device.getPassword(),
                fragment.getDomain(),
                fragment.getPort(),
                provisioningProperties.getCodecs(),
                fragment.getTimeout());
    }

    private OverrideFragmentDto getOverrideFragment(String overrideFragment) {
        OverrideFragmentDto fragment;
        try {
            fragment = objectMapper.readValue(overrideFragment, OverrideFragmentDto.class);
        } catch (Exception e) {
            throw new DataConversionException(e.getMessage());
        }
        return fragment;
    }

    private Properties getProps(String overrideFragment) {
        Properties p = new Properties();
        try {
            p.load(new StringReader(overrideFragment));
        } catch (Exception e) {
            throw new DataConversionException(e.getMessage());
        }
        return p;
    }

}
