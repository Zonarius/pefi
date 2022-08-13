package party.zonarius.pefibackend.script.executor;

import lombok.RequiredArgsConstructor;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.stereotype.Component;
import party.zonarius.pefibackend.db.entity.TransactionEntity;
import party.zonarius.pefibackend.db.repository.ApplicationRepository;
import party.zonarius.pefibackend.db.repository.TransactionRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScriptExecutor {
    private final ApplicationRepository applicationRepository;
    private final TransactionRepository transactionRepository;

    public void applyScript() {
        List<TransactionEntity> txEntities = transactionRepository.findAll();
        List<JsTransaction> txs = txEntities.stream()
            .map(TransactionEntity::toJsTransaction)
            .toList();

        List<String> tags = getTags(txs);

        for (int i = 0; i < txs.size(); i++) {
            txEntities.get(i).setTag(tags.get(i));
        }

        transactionRepository.saveAllAndFlush(txEntities);
    }

    private List<String> getTags(List<JsTransaction> txs) {
        String script = wrap(applicationRepository.getApplication().getScript());
        try (Context context = Context.create()) {
            Value getTag = context.eval("js", script);
            return txs.stream()
                .map(arguments -> getTag.execute(arguments).asString())
                .toList();
        }
    }

    private String wrap(String script) {
        return """
            (() => {
            """ + script +
            """
                return getTag
                })()
            """;
    }
}
