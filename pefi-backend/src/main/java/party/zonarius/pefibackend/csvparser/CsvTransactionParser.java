package party.zonarius.pefibackend.csvparser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CsvTransactionParser {
    public List<CsvTransaction> parse(InputStream stream) {
        try (
            InputStreamReader isr = new InputStreamReader(stream);
            BufferedReader reader = new BufferedReader(isr)
        ) {
            CsvToBean<CsvTransaction> parser = new CsvToBeanBuilder<CsvTransaction>(reader)
                .withSeparator(';')
                .withType(CsvTransaction.class)
                .build();
            return parser.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
