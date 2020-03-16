package com.voverc.provisioning.configuration;

import com.voverc.provisioning.domain.DeviceModel;
import com.voverc.provisioning.service.converter.ConfigurationDataConverter;
import com.voverc.provisioning.service.converter.JsonConfigurationDataConverter;
import com.voverc.provisioning.service.converter.PropsConfigurationDataConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;

@Configuration
public class ServiceConfiguration {

    @Bean
    public Map<DeviceModel, ConfigurationDataConverter> deviceModelToDataConverter(JsonConfigurationDataConverter jsonConfigurationDataConverter,
                                                                                   PropsConfigurationDataConverter propsConfigurationDataConverter) {
        EnumMap<DeviceModel, ConfigurationDataConverter> deviceModelToDataConverter = new EnumMap<>(DeviceModel.class);
        deviceModelToDataConverter.put(DeviceModel.CONFERENCE, jsonConfigurationDataConverter);
        deviceModelToDataConverter.put(DeviceModel.DESK, propsConfigurationDataConverter);
        return deviceModelToDataConverter;
    }

}
