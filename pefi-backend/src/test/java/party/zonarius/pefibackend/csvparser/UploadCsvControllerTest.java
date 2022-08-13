package party.zonarius.pefibackend.csvparser;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import party.zonarius.pefibackend.IntegrationTest;
import party.zonarius.pefibackend.TestUtil;
import party.zonarius.pefibackend.db.repository.TransactionRepository;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class UploadCsvControllerTest {
    private Expect expect;

    @Autowired
    private UploadCsvController uploadCsvController;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private WebApplicationContext ctx;

    @Test
    void uploadCsv() {
        assertThat(transactionRepository.count()).isEqualTo(0);

        given()
            .webAppContextSetup(ctx)
            .multiPart("file", TestUtil.getCsvExample())
        .when()
            .post("/uploadCsv")
        .then()
            .statusCode(200);

        expect.scenario("Transaction Count")
            .toMatchSnapshot(transactionRepository.count());
        expect.scenario("First transaction")
            .toMatchSnapshot(transactionRepository.findAll().get(0));
    }
}