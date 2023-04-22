package me.devksh930.stock.facade;

import me.devksh930.stock.repository.LockStockRepository;
import me.devksh930.stock.service.StockService;
import me.devksh930.stock.service.impl.DefaultStockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NameLockStockFacade implements StockService {

    private final LockStockRepository lockStockRepository;

    private final DefaultStockService defaultStockService;

    public NameLockStockFacade(final LockStockRepository lockStockRepository,
                               final DefaultStockService defaultStockService) {
        this.lockStockRepository = lockStockRepository;
        this.defaultStockService = defaultStockService;
    }

    @Transactional
    @Override
    public void decrease(final Long id, final Long quantity) {
        try {
            lockStockRepository.getLock(id.toString());
            defaultStockService.decrease(id, quantity);
        } finally {
            lockStockRepository.releaseLock(id.toString());
        }
    }
}
