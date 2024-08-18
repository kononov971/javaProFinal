package ru.vtb.dto;

import java.math.BigDecimal;

public record LimitDTO(Long userId, BigDecimal limitAmount) {
}
