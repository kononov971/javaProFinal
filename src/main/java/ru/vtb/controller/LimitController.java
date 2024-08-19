package ru.vtb.controller;

import org.springframework.web.bind.annotation.*;
import ru.vtb.dto.LimitDTO;
import ru.vtb.service.LimitService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/limits")
public class LimitController {

    private final LimitService limitService;

    public LimitController(LimitService limitService) {
        this.limitService = limitService;
    }

    @GetMapping("/{userId}")
    public LimitDTO getLimit(@PathVariable Long userId) {
        return limitService.getUserLimit(userId);
    }

    @PatchMapping("/{userId}")
    public LimitDTO changeLimit(@PathVariable Long userId, @RequestBody BigDecimal amount) {
        return limitService.setLimit(userId, amount);
    }

    @PostMapping("/decrease/{userId}")
    public LimitDTO decreaseLimit(@PathVariable Long userId, @RequestBody BigDecimal amount) {
        return limitService.decreaseUserLimit(userId, amount);
    }

    @PostMapping("/increase/{userId}")
    public LimitDTO increaseLimit(@PathVariable Long userId, @RequestBody BigDecimal amount) {
        return limitService.increaseUserLimit(userId, amount);
    }
}
