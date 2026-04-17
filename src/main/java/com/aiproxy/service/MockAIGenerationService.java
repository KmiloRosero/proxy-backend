package com.aiproxy.service;

import com.aiproxy.dto.GenerationRequest;
import com.aiproxy.dto.GenerationResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service("mockAIService")
public class MockAIGenerationService implements AIGenerationService {

    private static final String[] RESPONSES = {
            "In the quiet hum of laboratories, science turns curiosity into instruments that can measure the invisible.",
            "Technology is philosophy with a power supply: it makes our assumptions executable and our values scalable.",
            "Every hypothesis is a bridge between imagination and evidence, and every experiment is a step across it.",
            "The future isn’t predicted by machines; it’s negotiated by humans using machines as amplifiers of intent.",
            "If consciousness is a pattern, then meaning is the pressure we apply to shape that pattern into a story.",
            "From silicon to stars, progress is the art of compressing complexity into tools we can wield.",
            "Philosophy asks why; engineering answers how; science checks whether the answer survives reality.",
            "Knowledge expands like a universe: the more it grows, the more edges we discover to explore."
    };

    private final AtomicInteger responseIndex = new AtomicInteger(0);
    private final Random random = new Random();

    @Override
    public GenerationResponse generate(GenerationRequest request) throws Exception {
        long start = System.currentTimeMillis();

        Thread.sleep(1200);

        String prompt = request.prompt() == null ? "" : request.prompt();
        int estimatedTokens = Math.max(10, prompt.length() / 4 + random.nextInt(40) + 10);

        int index = Math.floorMod(responseIndex.getAndIncrement(), RESPONSES.length);
        String generatedText = RESPONSES[index];

        long processingTimeMs = System.currentTimeMillis() - start;
        return new GenerationResponse(
                generatedText,
                estimatedTokens,
                processingTimeMs,
                request.userId(),
                LocalDateTime.now()
        );
    }
}

