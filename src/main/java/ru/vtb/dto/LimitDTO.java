package ru.vtb.dto;

import java.math.BigDecimal;

public record LimitDTO(Long id, Long userId, BigDecimal limitAmount) {
}
