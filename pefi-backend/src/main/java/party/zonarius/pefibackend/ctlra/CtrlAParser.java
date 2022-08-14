package party.zonarius.pefibackend.ctlra;

import lombok.RequiredArgsConstructor;
import org.jparsec.Parser;
import org.jparsec.Parsers;
import org.springframework.stereotype.Component;
import party.zonarius.pefibackend.PefiBackendConfiguration;
import party.zonarius.pefibackend.db.entity.TransactionEntity;

import java.time.LocalDate;
import java.util.List;

import static org.jparsec.Scanners.*;

@Component
@RequiredArgsConstructor
public class CtrlAParser {
    private final PefiBackendConfiguration configuration;
    private final Parser<List<TransactionEntity>> parser = createParser();

    private Parser<List<TransactionEntity>> createParser() {
        var pre = ANY_CHAR.until(string("Beleg")).next(string("Beleg")).label("Preamble");
        return pre.next(tx().many1().label("txlist"))
            .followedBy(ANY_CHAR.many().label("ending"));
    }

    private Parser<TransactionEntity> tx() {
        var txPre = string("Beleg").optional(null).next(WHITESPACES);
        return Parsers.sequence(
            txPre.next(date()).followedBy(WHITESPACES).label("date"),
            description(),
            transactionDate().label("actual"),
            WHITESPACES.next(amount()),
            (paymentDate, description, transactionDate, amount) -> TransactionEntity.builder()
                .currency("EUR")
                .amount(amount)
                .paymentDate(paymentDate)
                .transactionDate(transactionDate)
                .description(description)
                .build()
        ).label("Transaction");
    }

    private Parser<String> description() {
        return ANY_CHAR.source().until(transactionDate().next(WHITESPACES).next(amount()).label("skipped"))
            .map(this::toString)
            .label("description");
    }

    private String toString(List<String> chars) {
        return chars.stream()
            .reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append)
            .toString();
    }

    private Parser<LocalDate> date() {
        return Parsers.sequence(
            INTEGER.followedBy(string(".")),
            INTEGER.followedBy(string(".")),
            INTEGER,
            (day, month, year) -> LocalDate.of(
                Integer.parseInt(year),
                Integer.parseInt(month),
                Integer.parseInt(day)
            )
        ).label("Date");
    }

    private Parser<LocalDate> transactionDate() {
        var month = INTEGER.next(string("/")).next(INTEGER).label("txMonth");
        return Parsers.or(
            date().followedBy(WHITESPACES).followedBy(month),
            date()
        ).label("transactionDate");
    }

    private Parser<Integer> amount() {
        var num = Parsers.sequence(
            INTEGER.sepBy1(string(".")).map(nrs -> {
                int sum = 0;
                for (String nr : nrs) {
                    sum = sum * 1000 + Integer.parseInt(nr);
                }
                return sum;
            }),
            string(",").next(INTEGER), (eur, cents) ->
                eur * 100 + Integer.parseInt(cents)
            );
        var negative = string("-").next(num).map(x -> -x);
        return Parsers.or(negative, num).followedBy(newLine()).label("amount");
    }

    private Parser<Void> newLine() {
        return Parsers.or(
            string("\r\n"),
            string("\n"),
            string("\r")
        ).label("newline");
    }

    public List<TransactionEntity> parse(String input) {
        List<TransactionEntity> parsed = parser.parse(input);
        parsed.forEach(tx -> tx.setIban(configuration.getDefaultIban()));
        return parsed;
    }
}
