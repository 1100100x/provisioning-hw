package com.voverc.provisioning.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverrideFragmentDto {

    private String domain;
    private Integer port;
    private Integer timeout;
}
