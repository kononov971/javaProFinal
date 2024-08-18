package ru.vtb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.vtb.entity.Limit;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {
    Optional<Limit> findByUserId(Long userId);

    @Modifying
    @Query(value = "update limits set limit_amount = ?1", nativeQuery = true)
    void setDefaultLimitAmount(BigDecimal defaultLimitAmount);
}
