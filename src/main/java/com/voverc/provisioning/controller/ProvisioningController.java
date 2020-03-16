package com.voverc.provisioning.controller;

import com.voverc.provisioning.service.ProvisioningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
@RestController
public class ProvisioningController {

    private final ProvisioningService provisioningService;

    @GetMapping(value = "/provisioning/{macAddress}")
    public ResponseEntity<String> getConfig(@PathVariable String macAddress) throws Exception {
        return ResponseEntity.ok(provisioningService.getProvisioningFile(macAddress));
    }
}
