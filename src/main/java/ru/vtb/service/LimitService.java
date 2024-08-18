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
    private final RestTemplate restTemplate;

    public LimitService(LimitRepository limitRepository, LimitsProperties limitsProperties, RestTemplate restTemplate) {
        this.limitRepository = limitRepository;
        this.limitsProperties = limitsProperties;
        this.restTemplate = restTemplate;
    }

    public LimitDTO getLimit(Long userId) {
        Limit limit = limitRepository.findByUserId(userId).orElse(newLimit(userId));

        return limitDTOFromLimit(limit);
    }

    public LimitDTO makePayment(Long userId, BigDecimal paymentAmount) {
        Limit limit = limitRepository.findByUserId(userId).orElse(newLimit(userId));
        BigDecimal valueLimit = limit.getLimitAmount();

        if (valueLimit.compareTo(paymentAmount) < 0) {
            throw new LimitException("Current limit of user " + userId + " is " + valueLimit + ", but payment amount is " + paymentAmount);
        } else {
            limit.setLimitAmount(valueLimit.subtract(paymentAmount));
            limitRepository.save(limit);
            limitRepository.flush();
        }

        if (!makeTransaction(userId, paymentAmount)) {
            limit.setLimitAmount(valueLimit);
            limitRepository.save(limit);
            throw new TransactionException("Transaction not executed");
        }

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

    private boolean makeTransaction(Long userId, BigDecimal paymentAmount) {
        TransactionResponseDTO response;
        try {
            response = restTemplate.postForObject("/execute/" + userId, paymentAmount, TransactionResponseDTO.class);
        } catch (HttpClientErrorException e) {
            throw new TransactionException(e.getMessage());
        }

        return response.isSuccess();
    }

    private LimitDTO limitDTOFromLimit(Limit limit) {
        return new LimitDTO(
                limit.getUserId(),
                limit.getLimitAmount()
        );
    }
}
