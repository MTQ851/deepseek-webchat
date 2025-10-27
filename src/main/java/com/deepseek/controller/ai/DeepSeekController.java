package com.deepseek.controller.ai;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletRequest;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deepseek")
@Slf4j
public class DeepSeekController {
    //输入你的key
    private static final String DEEP_SEEK_API_KEY = "Bearer sk-xxxxxxxxxxxxxxxx";


    private final WebClient webClientByDeepSeek = WebClient.builder()
            .baseUrl("https://api.deepseek.com")
            .defaultHeader("Authorization", DEEP_SEEK_API_KEY)
            .build();

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestParam String prompt, HttpServletRequest request) {
        //todo 历史记录可加上下文联系逻辑自行补充
        List<Map<String, String>> chatHistory = new LinkedList<>();
        // 添加用户输入
        Map<String, String> userInput = new HashMap<>();
        userInput.put("role", "user");
        userInput.put("content", prompt);
        chatHistory.add(userInput);

        // 组装 OpenAI API 参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("model", "deepseek-chat");
        paramMap.put("messages", chatHistory);
        paramMap.put("stream", true);
        ObjectMapper objectMapper = new ObjectMapper();
        StringBuilder answerBuffer = new StringBuilder();
        StringBuilder answerThinkBuffer = new StringBuilder();
        return webClientByDeepSeek.post()
                .uri("/v1/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(paramMap)
                .retrieve()
                .bodyToFlux(String.class)
                .doOnNext(json -> {
                    // 跳过 [DONE] 标记，避免 JSON 解析错误
                    if ("[DONE]".equals(json)) {
                        return;
                    }
                    // 保持返回内容不变，但提取回答保存
                    try {
                        // 解析 JSON 文本为树形结构
                        JsonNode root = objectMapper.readTree(json);

                        // 提取聊天回答内容（模型输出）
                        String extracted = root.path("choices").get(0).path("delta").path("content").asText("");
                        answerBuffer.append(extracted);

                        // 提取模型推理内容（有些模型返回 reasoning_content）
                        String extractedThink = root.path("choices").get(0).path("delta").path("reasoning_content").asText("");
                        answerThinkBuffer.append(extractedThink);


                    } catch (JsonProcessingException e) {
                        log.error("解析回答失败: {}", json, e);
                    }
                })
                .doFinally(signal -> {
                    //todo 可补充保存内容逻辑
                    log.info("模型输出: " + answerBuffer);
                    log.info("模型推理: " + answerThinkBuffer);
                })
                .delayElements(Duration.ofMillis(50)); // 控制节奏

    }


}
