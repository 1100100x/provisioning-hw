package com.voverc.provisioning.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "provisioning")
public class ProvisioningProperties {

    @NotBlank
    private String domain;

    @NotNull
    @Positive
    private int port;

    @NotEmpty
    private List<String> codecs;
}
