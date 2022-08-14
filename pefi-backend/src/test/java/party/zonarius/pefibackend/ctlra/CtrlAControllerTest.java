package party.zonarius.pefibackend.ctlra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import party.zonarius.pefibackend.IntegrationTest;
import party.zonarius.pefibackend.TestResources;
import party.zonarius.pefibackend.TestUtil;
import party.zonarius.pefibackend.db.entity.TransactionEntity;
import party.zonarius.pefibackend.db.repository.TransactionRepository;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@IntegrationTest
class CtrlAControllerTest {
    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CtrlAParser ctrlAParser;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAllInBatch();
    }

    @Test
    void parserError() {
        given()
            .webAppContextSetup(ctx)
            .contentType("application/json")
            .body(new CtrlARequest("1234"))
        .when()
            .post("/api/ctrla")
        .then()
            .statusCode(400)
            .body("type", equalTo("parser_error"))
            .body("message", equalTo("line 1, column 5:\nany character or Beleg expected, EOF encountered."));
    }

    @Test
    void notOverlapping() {
        given()
            .webAppContextSetup(ctx)
            .contentType("application/json")
            .body(new CtrlARequest(TestResources.CtrlA.directExample()))
        .when()
            .post("/api/ctrla")
        .then()
            .statusCode(400)
            .body("type", equalTo("not_overlapping"));
    }

    @Test
    void allExisting() {
        create(TestResources.CtrlA.directExample());

        given()
            .webAppContextSetup(ctx)
            .contentType("application/json")
            .body(new CtrlARequest(TestResources.CtrlA.directExample()))
        .when()
            .post("/api/ctrla")
        .then()
            .statusCode(200)
            .body("type", equalTo("saved"))
            .body("itemsSaved", equalTo(0));
    }

    @Test
    void partial() {
        create(TestResources.CtrlA.partial());

        assertThat(transactionRepository.count()).isEqualTo(25);

        given()
            .webAppContextSetup(ctx)
            .contentType("application/json")
            .body(new CtrlARequest(TestResources.CtrlA.directExample()))
            .when()
            .post("/api/ctrla")
            .then()
            .statusCode(200)
            .body("type", equalTo("saved"))
            .body("itemsSaved", equalTo(5));

        List<TransactionEntity> txs = transactionRepository.findAll();

        assertThat(txs).hasSize(30);
        assertThat(txs).allSatisfy(tx -> assertThat(tx.getIban()).isEqualTo("AT320000000000000000"));
    }

    private void create(String input) {
        List<TransactionEntity> txs = ctrlAParser.parse(input);
        transactionRepository.saveAllAndFlush(txs);
    }
}