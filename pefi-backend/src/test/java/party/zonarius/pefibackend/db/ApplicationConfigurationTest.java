package party.zonarius.pefibackend.db;

import au.com.origin.snapshots.junit5.SnapshotExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import party.zonarius.pefibackend.IntegrationTest;
import party.zonarius.pefibackend.db.entity.ApplicationEntity;
import party.zonarius.pefibackend.db.repository.ApplicationRepository;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@ExtendWith({SnapshotExtension.class})
class ApplicationConfigurationTest {
    @Autowired
    private ApplicationRepository applicationRepository;

    @Test
    void applicationConfigurationCreated() {
        ApplicationEntity application = applicationRepository.getApplication();
        assertThat(application.getId()).isEqualTo(1);
        assertThat(application.getScript()).isNotEmpty();
    }
}