package party.zonarius.pefibackend.db.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "application")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApplicationEntity {
    @Id
    private int id;

    @Column(length = 65536)
    private String script;
}
