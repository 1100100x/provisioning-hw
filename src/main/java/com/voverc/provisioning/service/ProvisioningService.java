package com.voverc.provisioning.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ProvisioningService {
    String getProvisioningFile(String macAddress) throws JsonProcessingException;
}
