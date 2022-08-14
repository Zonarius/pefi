package party.zonarius.pefibackend.ctlra;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import au.com.origin.snapshots.serializers.SnapshotSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import party.zonarius.pefibackend.PefiBackendConfiguration;
import party.zonarius.pefibackend.TestResources;
import party.zonarius.pefibackend.db.entity.TransactionEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({SnapshotExtension.class})
class CtrlAParserTest {
    private Expect expect;
    private CtrlAParser parser;

    @BeforeEach
    void setUp() {
        parser = new CtrlAParser(new PefiBackendConfiguration());
    }

    @Test
    void snapshot() {
        String ctrla = TestResources.CtrlA.directExample();
        List<TransactionEntity> result = parser.parse(ctrla);
        assertThat(result).hasSize(30);
        expect.serializer("json").toMatchSnapshot(result);
    }
}