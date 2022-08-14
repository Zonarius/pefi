package party.zonarius.pefibackend;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("party.zonarius.pefibackend.configuration")
public class PefiBackendConfiguration {
    private String defaultIban = "<unknown>";
}
