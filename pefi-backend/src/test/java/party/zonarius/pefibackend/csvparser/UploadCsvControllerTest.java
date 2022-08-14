package party.zonarius.pefibackend.csvparser;

import au.com.origin.snapshots.Expect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import party.zonarius.pefibackend.IntegrationTest;
import party.zonarius.pefibackend.TestResources;
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

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAllInBatch();
    }

    @Test
    void uploadCsv() {
        assertThat(transactionRepository.count()).isEqualTo(0);

        given()
            .webAppContextSetup(ctx)
            .multiPart("file", TestResources.Csv.exampleAsFile())
        .when()
            .post("/api/uploadCsv")
        .then()
            .statusCode(200);

        expect.scenario("Transaction Count")
            .toMatchSnapshot(transactionRepository.count());
        expect.scenario("First transaction")
            .toMatchSnapshot(transactionRepository.findAll().get(0));
    }
}