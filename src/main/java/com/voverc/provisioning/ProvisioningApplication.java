package com.voverc.provisioning;

import com.voverc.provisioning.properties.ProvisioningProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableConfigurationProperties(value = ProvisioningProperties.class)
@EnableWebMvc
@SpringBootApplication
public class ProvisioningApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProvisioningApplication.class, args);
    }
}
