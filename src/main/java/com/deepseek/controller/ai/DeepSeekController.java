package com.deepseek.controller.ai;


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
public class DeepSeekController {
    private static final String DEEP_SEEK_API_KEY = "Bearer sk-eb0fbb4b860f434daf1f93dec1df8eba";
    //输入你的key
//    private static final String DEEP_SEEK_API_KEY = "Bearer ";


    private final WebClient webClientByDeepSeek = WebClient.builder()
            .baseUrl("https://api.deepseek.com/v1")
            .defaultHeader("Authorization", DEEP_SEEK_API_KEY)
            .build();

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestParam String prompt, HttpServletRequest request) {
        //todo 历史记录逻辑自行补充
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

        return webClientByDeepSeek.post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(paramMap)
                .retrieve()
                .bodyToFlux(String.class)
                .delayElements(Duration.ofMillis(50));
    }


}
