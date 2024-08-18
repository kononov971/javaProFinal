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

    @GetMapping("/{id}")
    public LimitDTO getLimit(@PathVariable Long id) {
        return limitService.getLimit(id);
    }

    @PostMapping("/{id}")
    public LimitDTO makePayment(@PathVariable Long id, @RequestBody BigDecimal paymentAmount) {
        return limitService.makePayment(id, paymentAmount);
    }

}
