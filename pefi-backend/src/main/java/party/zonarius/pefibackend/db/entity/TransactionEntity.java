package party.zonarius.pefibackend.db.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import party.zonarius.pefibackend.script.executor.JsTransaction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Objects;

@Entity(name = "transaction")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class
TransactionEntity {
    @Id
    @GeneratedValue(generator = "hash-generator")
    @GenericGenerator(name = "hash-generator", strategy = "party.zonarius.pefibackend.db.TransactionHashGenerator")
    private String hash;

    @Column()
    private String tag;

    @Column(nullable = false)
    private String iban;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate paymentDate;

    @Column(nullable = false)
    private LocalDate transactionDate;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private String currency;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TransactionEntity that = (TransactionEntity) o;
        return hash != null && Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public JsTransaction toJsTransaction() {
        return JsTransaction.builder()
            .iban(iban)
            .description(description)
            .paymentDate(paymentDate.toString())
            .transactionDate(transactionDate.toString())
            .amount(amount / 100.0)
            .currency(currency)
            .build();
    }
}

