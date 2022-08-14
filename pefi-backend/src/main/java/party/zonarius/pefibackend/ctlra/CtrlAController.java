package party.zonarius.pefibackend.ctlra;

import lombok.RequiredArgsConstructor;
import org.jparsec.error.ParserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import party.zonarius.pefibackend.db.TransactionHashComponent;
import party.zonarius.pefibackend.db.entity.TransactionEntity;
import party.zonarius.pefibackend.db.repository.TransactionRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/ctrla")
@RequiredArgsConstructor
public class CtrlAController {
    private final CtrlAParser parser;
    private final TransactionRepository transactionRepository;
    private final TransactionHashComponent hasher;

    @PostMapping
    public ResponseEntity<CtrlAResponse> ctrla(@RequestBody CtrlARequest ctrlARequest) {
        List<TransactionEntity> parsed;
        try {
            parsed = parser.parse(ctrlARequest.getContent());
        } catch (ParserException ex) {
            return new CtrlAResponse.ParserError(ex).toResponseEntity();
        }

        parsed.forEach(hasher::setHash);

        List<String> strings = parsed.stream().map(TransactionEntity::getHash).toList();
        Set<TransactionEntity> found = new HashSet<>(transactionRepository.findAllById(strings));
        if (found.isEmpty()) {
            return new CtrlAResponse.NotOverlapping().toResponseEntity();
        }

        List<TransactionEntity> toBeSaved = parsed.stream().filter(tx -> !found.contains(tx)).toList();

        transactionRepository.saveAllAndFlush(toBeSaved);

        return new CtrlAResponse.Saved(toBeSaved.size()).toResponseEntity();
    }

}
