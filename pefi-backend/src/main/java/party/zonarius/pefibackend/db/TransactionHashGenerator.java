package party.zonarius.pefibackend.db;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import party.zonarius.pefibackend.db.entity.TransactionEntity;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TransactionHashGenerator implements IdentifierGenerator {
    private final MessageDigest messageDigest;

    public TransactionHashGenerator() throws NoSuchAlgorithmException {
        this.messageDigest = MessageDigest.getInstance("sha-256");
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        TransactionEntity tx = (TransactionEntity) o;
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
