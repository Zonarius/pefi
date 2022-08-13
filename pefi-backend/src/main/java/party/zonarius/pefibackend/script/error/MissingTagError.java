package party.zonarius.pefibackend.script.error;

import lombok.Getter;
import party.zonarius.pefibackend.db.entity.TransactionEntity;

@Getter
public class MissingTagError extends ScriptError {
    private final TransactionEntity transaction;

    public MissingTagError(TransactionEntity transaction) {
        super(ScriptErrorType.MISSING_TAG_ERROR);
        this.transaction = transaction;
    }
}
