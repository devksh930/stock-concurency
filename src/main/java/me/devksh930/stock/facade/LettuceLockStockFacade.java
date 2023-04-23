package me.devksh930.stock.facade;

import me.devksh930.stock.repository.RedisLockRepository;
import me.devksh930.stock.service.StockService;
import me.devksh930.stock.service.impl.DefaultStockService;
import org.springframework.stereotype.Component;

@Component
public class LettuceLockStockFacade  {
    private final RedisLockRepository redisLockRepository;
    private final DefaultStockService defaultStockService;

    public LettuceLockStockFacade(
            final RedisLockRepository redisLockRepository,
            final DefaultStockService defaultStockService) {
        this.redisLockRepository = redisLockRepository;
        this.defaultStockService = defaultStockService;
    }

    public void decrease(Long key, Long quantity) throws InterruptedException {
        while (!redisLockRepository.lock(key)) {
            Thread.sleep(100);
        }

        try {
            defaultStockService.decrease(key, quantity);
        } finally {
            redisLockRepository.unLock(key);
        }
    }
}
