package party.zonarius.pefibackend.db;

import org.springframework.stereotype.Component;
import party.zonarius.pefibackend.db.entity.TransactionEntity;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class TransactionHashComponent {
    private final MessageDigest messageDigest;

    public TransactionHashComponent() {
        try {
            this.messageDigest = MessageDigest.getInstance("sha-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String hash(TransactionEntity tx) {
        try {
            ByteArrayOutputStream bsas = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bsas);
            oos.writeObject(tx.getIban());
            oos.writeObject(tx.getDescription());
            oos.writeObject(tx.getPaymentDate());
            oos.writeObject(tx.getTransactionDate());
            oos.write(tx.getAmount());
            oos.writeObject(tx.getCurrency());

            byte[] digest = messageDigest.digest(bsas.toByteArray());
            return DatatypeConverter.printHexBinary(digest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
