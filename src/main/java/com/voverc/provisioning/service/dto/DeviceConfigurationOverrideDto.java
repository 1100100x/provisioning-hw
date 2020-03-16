package com.voverc.provisioning.service.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DeviceConfigurationOverrideDto extends DeviceConfigurationDto {

    private Integer timeout;

    public DeviceConfigurationOverrideDto(String username, String password, String domain, Integer port, List<String> codecs, Integer timeout) {
        super(username, password, domain, port, codecs);
        this.timeout = timeout;
    }
}
