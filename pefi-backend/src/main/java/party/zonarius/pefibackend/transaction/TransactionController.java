package party.zonarius.pefibackend.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import party.zonarius.pefibackend.db.entity.TransactionEntity;
import party.zonarius.pefibackend.db.repository.TransactionRepository;

import java.util.List;

@RestController
@RequestMapping("api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionRepository transactionRepository;

    @GetMapping
    public TransactionResponse transactions() {
        List<TransactionEntity> transactions = transactionRepository.findAll();
        return new TransactionResponse(transactions);
    }
}
