package com.minimart.inventorysvc.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class Notifier {

    private final StringRedisTemplate redis;

    public void enqueueSuccess(Long orderId) {
        redis.opsForStream().add("mails",
                Map.of(
                        "template", "OrderConfirmed",
                        "orderId", orderId.toString()));
        log.info("--- enqueued order confirmed for {}", orderId);
    }

    public void enqueueFailure(Long orderId) {
        redis.opsForStream().add("mails",
                Map.of(
                        "template", "OrderFailed",
                        "orderId", orderId.toString()));
        log.info("--- enqueued order failed for {}", orderId);
    }
}
