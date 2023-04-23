package me.devksh930.stock.service;

import me.devksh930.stock.domain.Stock;
import me.devksh930.stock.facade.LettuceLockStockFacade;
import me.devksh930.stock.facade.RedissonLockStockFacade;
import me.devksh930.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private RedissonLockStockFacade stockService;


    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void before() {
        final Stock stock = new Stock(1L, 100L);
        stockRepository.saveAndFlush(stock);
    }

    @AfterEach
    public void after() {
        stockRepository.deleteAll();
    }

    @Test
    public void stock_decrease() throws InterruptedException {
        stockService.decrease(1L, 1L);

        final Stock stock = stockRepository.findById(1L).orElseThrow();

        assertEquals(99, stock.getQuantity());
    }

    @Test
    @DisplayName("동시에 한 자원에 접근 레이스컨디션")
    public void stock_decrease_thread100() throws InterruptedException {
        final int treadCount = 100;

        final ExecutorService executorService = Executors.newFixedThreadPool(32);
        final CountDownLatch countDownLatch = new CountDownLatch(treadCount);

        for (int i = 0; i < treadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decrease(1L, 1L);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        final Stock stock = stockRepository.findById(1L).orElseThrow();

        assertEquals(0L, stock.getQuantity());
    }
}