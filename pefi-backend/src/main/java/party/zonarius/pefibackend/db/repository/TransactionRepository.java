package party.zonarius.pefibackend.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import party.zonarius.pefibackend.db.entity.TransactionEntity;

import java.util.List;
import java.util.Set;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {

    @Query("from transaction tx " +
        "where tx.tag is null")
    List<TransactionEntity> findAllWithMissingTag();
}
