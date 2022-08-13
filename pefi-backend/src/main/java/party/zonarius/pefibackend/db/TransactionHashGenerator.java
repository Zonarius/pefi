package party.zonarius.pefibackend.db;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import party.zonarius.pefibackend.db.entity.TransactionEntity;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

public class TransactionHashGenerator implements IdentifierGenerator {
    private final TransactionHashComponent txHasher;

    public TransactionHashGenerator() throws NoSuchAlgorithmException {
        this.txHasher = new TransactionHashComponent();
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return txHasher.hash(((TransactionEntity) o));
    }
}
