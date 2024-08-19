package ru.vtb.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.vtb.config.properties.LimitsProperties;
import ru.vtb.dto.LimitDTO;
import ru.vtb.dto.TransactionResponseDTO;
import ru.vtb.entity.Limit;
import ru.vtb.exception.LimitException;
import ru.vtb.exception.TransactionException;
import ru.vtb.repository.LimitRepository;

import java.math.BigDecimal;

@Service
public class LimitService {

    private final LimitRepository limitRepository;
    private final LimitsProperties limitsProperties;

    public LimitService(LimitRepository limitRepository, LimitsProperties limitsProperties) {
        this.limitRepository = limitRepository;
        this.limitsProperties = limitsProperties;
    }

    public LimitDTO getUserLimit(Long userId) {
        Limit limit = limitRepository.findByUserId(userId).orElse(newLimit(userId));

        return limitDTOFromLimit(limit);
    }

    public LimitDTO increaseUserLimit(Long userId, BigDecimal amount) {
        Limit limit = limitRepository.findByUserId(userId).orElse(newLimit(userId));

        limit.setLimitAmount(limit.getLimitAmount().add(amount));
        limitRepository.save(limit);

        return limitDTOFromLimit(limit);
    }

    public LimitDTO decreaseUserLimit(Long userId, BigDecimal amount) {
        Limit limit = limitRepository.findByUserId(userId).orElse(newLimit(userId));

        if (limit.getLimitAmount().compareTo(amount) < 0) {
            throw new LimitException("Current limit of user " + userId + " is " + limit.getLimitAmount() + ", but payment amount is " + amount);
        } else {
            limit.setLimitAmount(limit.getLimitAmount().subtract(amount));
            limitRepository.save(limit);
        }

        return limitDTOFromLimit(limit);
    }

    public LimitDTO setLimit(Long userId, BigDecimal amount) {
        Limit limit = limitRepository.findByUserId(userId).orElse(newLimit(userId));

        limit.setLimitAmount(amount);
        limitRepository.save(limit);

        return limitDTOFromLimit(limit);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void setDefaultLimits() {
        limitRepository.setDefaultLimitAmount(limitsProperties.getDefaultValue());
    }

    private Limit newLimit(Long userId) {
        Limit limit = new Limit();
        limit.setUserId(userId);
        limit.setLimitAmount(limitsProperties.getDefaultValue());

        return limit;
    }

    private LimitDTO limitDTOFromLimit(Limit limit) {
        return new LimitDTO(
                limit.getId(),
                limit.getUserId(),
                limit.getLimitAmount()
        );
    }
}
