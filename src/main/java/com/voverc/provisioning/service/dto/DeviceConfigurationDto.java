package com.voverc.provisioning.service.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Data
public class DeviceConfigurationDto {

    private String username;

    private String password;

    private String domain;

    private Integer port;

    private List<String> codecs;

}


