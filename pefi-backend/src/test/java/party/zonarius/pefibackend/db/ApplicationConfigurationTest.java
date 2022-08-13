package party.zonarius.pefibackend.db;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import party.zonarius.pefibackend.IntegrationTest;
import party.zonarius.pefibackend.db.entity.ApplicationEntity;
import party.zonarius.pefibackend.db.repository.ApplicationRepository;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class ApplicationConfigurationTest {
    @Autowired
    private ApplicationRepository applicationRepository;

    @Test
    void applicationConfigurationCreated() {
        ApplicationEntity application = applicationRepository.getApplication();
        assertThat(application.getId()).isEqualTo(1);
        assertThat(application.getScript()).isEmpty();
    }
}