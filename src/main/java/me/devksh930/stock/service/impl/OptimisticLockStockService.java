package me.devksh930.stock.service.impl;

import me.devksh930.stock.domain.Stock;
import me.devksh930.stock.repository.StockRepository;
import me.devksh930.stock.service.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptimisticLockStockService implements StockService {
    private final StockRepository stockRepository;

    public OptimisticLockStockService(final StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    @Override
    public void decrease(final Long id, final Long quantity) {
        final Stock stock = stockRepository.findByIdWithOptimisticLock(id);
        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }
}
