package me.devksh930.stock.facade;

import me.devksh930.stock.service.StockService;
import me.devksh930.stock.service.impl.OptimisticLockStockService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class OptimisticLockStopFacade implements StockService {

    private final OptimisticLockStockService optimisticLockStockService;


    public OptimisticLockStopFacade(final OptimisticLockStockService optimisticLockStockService) {
        this.optimisticLockStockService = optimisticLockStockService;
    }

    public void decrease(final Long id, final Long quantity) {
        while (true) {
            try {
                optimisticLockStockService.decrease(id, quantity);
                break;
            } catch (Exception e) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}
