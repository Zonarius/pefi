package party.zonarius.pefibackend.csvparser;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvNumber;
import lombok.Data;
import party.zonarius.pefibackend.db.entity.TransactionEntity;

import java.time.LocalDate;

@Data
public class CsvTransaction {
    @CsvBindByPosition(position = 0)
    private String iban;

    @CsvBindByPosition(position = 1)
    private String description;

    @CsvBindByPosition(position = 2)
    @CsvDate("dd.MM.yyyy")
    private LocalDate paymentDate;

    @CsvBindByPosition(position = 3)
    @CsvDate("dd.MM.yyyy")
    private LocalDate transactionDate;

    @CsvBindByPosition(position = 4, locale = "de-DE")
    @CsvNumber("+#.##;-#.##")
    private double amount;

    @CsvBindByPosition(position = 5)
    private String currency;

    public TransactionEntity toEntity() {
        return TransactionEntity.builder()
            .iban(iban)
            .description(description)
            .paymentDate(paymentDate)
            .transactionDate(transactionDate)
            .amount((int) (amount * 100))
            .currency(currency)
            .build();
    }
}
