package com.aiproxy.dto;

import java.time.LocalDateTime;

public record GenerationResponse(
        String generatedText,
        int tokensUsed,
        long processingTimeMs,
        String userId,
        LocalDateTime timestamp
) {
}

