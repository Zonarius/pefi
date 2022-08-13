package party.zonarius.pefibackend.csvparser;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import party.zonarius.pefibackend.db.entity.TransactionEntity;
import party.zonarius.pefibackend.db.repository.TransactionRepository;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class UploadCsvController {
    private final CsvTransactionParser parser;
    private final TransactionRepository transactionRepository;

    @PostMapping(value = "/uploadCsv", consumes = "multipart/form-data")
    public void uploadCsv(
        @RequestParam("file") MultipartFile file
    ) throws IOException {
        List<CsvTransaction> parsed = parser.parse(file.getInputStream());
        List<TransactionEntity> entities = parsed.stream()
            .map(CsvTransaction::toEntity)
            .toList();

        transactionRepository.saveAllAndFlush(entities);
    }
}
