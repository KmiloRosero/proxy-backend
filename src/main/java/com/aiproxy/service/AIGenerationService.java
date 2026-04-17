package com.aiproxy.service;

import com.aiproxy.dto.GenerationRequest;
import com.aiproxy.dto.GenerationResponse;

/** Proxy Pattern Subject: implemented by real service and both proxies */
public interface AIGenerationService {

    GenerationResponse generate(GenerationRequest request) throws Exception;
}

