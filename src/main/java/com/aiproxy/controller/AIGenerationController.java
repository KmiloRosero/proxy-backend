package com.aiproxy.controller;

import com.aiproxy.dto.GenerationRequest;
import com.aiproxy.dto.GenerationResponse;
import com.aiproxy.service.ProxyChainFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin
public class AIGenerationController {

    private final ProxyChainFactory proxyChainFactory;

    public AIGenerationController(ProxyChainFactory proxyChainFactory) {
        this.proxyChainFactory = proxyChainFactory;
    }

    @PostMapping("/generate")
    public ResponseEntity<GenerationResponse> generate(@RequestBody GenerationRequest request) throws Exception {
        GenerationResponse response = proxyChainFactory.buildChain().generate(request);
        return ResponseEntity.ok(response);
    }
}

