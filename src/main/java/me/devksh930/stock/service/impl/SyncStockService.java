package me.devksh930.stock.service.impl;

import me.devksh930.stock.domain.Stock;
import me.devksh930.stock.repository.StockRepository;
import me.devksh930.stock.service.StockService;
import org.springframework.stereotype.Service;

@Service
public class SyncStockService implements StockService {

    private final StockRepository stockRepository;

    public SyncStockService(final StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }


    @Override
    //    @Transactional
    public synchronized void decrease(final Long id, final Long quantity) {
        final Stock stock = stockRepository.findById(id).orElseThrow();

        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }
}
