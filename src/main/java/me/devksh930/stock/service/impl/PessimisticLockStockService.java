package me.devksh930.stock.service.impl;

import me.devksh930.stock.domain.Stock;
import me.devksh930.stock.repository.StockRepository;
import me.devksh930.stock.service.StockService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PessimisticLockStockService implements StockService {
    private final StockRepository stockRepository;

    public PessimisticLockStockService(final StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    @Transactional
    public void decrease(final Long id, final Long quantity) {
        final Stock stock = stockRepository.findByIdWithPessimisticLock(id);

        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }
}
