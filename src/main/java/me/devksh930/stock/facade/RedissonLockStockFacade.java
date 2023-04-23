package me.devksh930.stock.facade;

import me.devksh930.stock.service.impl.DefaultStockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedissonLockStockFacade {
    private final RedissonClient redissonClient;
    private final DefaultStockService defaultStockService;

    public RedissonLockStockFacade(final RedissonClient redissonClient, final DefaultStockService defaultStockService) {
        this.redissonClient = redissonClient;
        this.defaultStockService = defaultStockService;
    }

    public void decrease(Long key, Long quantity) {
        final RLock lock = redissonClient.getLock(key.toString());

        try {
            final boolean available = lock.tryLock(5, 1, TimeUnit.SECONDS);
            if (!available) {
                System.out.println("Lock획득");
                return;
            }
            defaultStockService.decrease(key, quantity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
