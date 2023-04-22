package me.devksh930.stock.service.impl;

import me.devksh930.stock.domain.Stock;
import me.devksh930.stock.repository.StockRepository;
import me.devksh930.stock.service.StockService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultStockService implements StockService {
    private final StockRepository stockRepository;

    public DefaultStockService(final StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void decrease(final Long id, final Long quantity) {
        final Stock stock = stockRepository.findById(id).orElseThrow();

        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }
}
