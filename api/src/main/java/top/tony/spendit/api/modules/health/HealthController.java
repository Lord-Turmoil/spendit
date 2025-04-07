/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.health;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tony.spendit.api.common.dto.MessageResponse;

@RestController
@RequestMapping("health")
@RequiredArgsConstructor
@Slf4j
public class HealthController {
    @GetMapping("ping")
    @Operation(summary = "Ping the server")
    public MessageResponse ping() {
        return MessageResponse.ok("pong");
    }
}
