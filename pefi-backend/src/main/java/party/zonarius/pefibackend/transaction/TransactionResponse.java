package party.zonarius.pefibackend.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import party.zonarius.pefibackend.db.entity.TransactionEntity;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private List<TransactionEntity> transactions;
}
