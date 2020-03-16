package com.voverc.provisioning.service.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voverc.provisioning.exception.DataConversionException;
import com.voverc.provisioning.service.dto.DeviceConfigurationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonConfigurationDataConverter implements ConfigurationDataConverter {

    private final ObjectMapper objectMapper;

    public String convert(DeviceConfigurationDto deviceConfigurationDto) {
        try {
            return objectMapper.writeValueAsString(deviceConfigurationDto);
        } catch (JsonProcessingException e) {
            throw new DataConversionException(e.getMessage());
        }
    }
}
