package party.zonarius.pefibackend.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import party.zonarius.pefibackend.db.entity.ApplicationEntity;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Integer> {
    @Query("from application a " +
        "where a.id = 1")
    ApplicationEntity getApplication();
}
