package party.zonarius.pefibackend.ctlra;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import party.zonarius.pefibackend.TestUtil;
import party.zonarius.pefibackend.db.entity.TransactionEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({SnapshotExtension.class})
class CtrlAParserTest {
    private Expect expect;
    private CtrlAParser parser;

    @BeforeEach
    void setUp() {
        parser = new CtrlAParser();
    }

    @Test
    void snapshot() {
        String ctrla = TestUtil.getResource("/sensitive-test-data/ctrla");
        List<TransactionEntity> result = parser.parse(ctrla);
        assertThat(result).hasSize(30);
        expect.toMatchSnapshot(result);
    }
}