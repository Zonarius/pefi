package party.zonarius.pefibackend.script;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import party.zonarius.pefibackend.IntegrationTest;
import party.zonarius.pefibackend.TestResources;
import party.zonarius.pefibackend.TestUtil;
import party.zonarius.pefibackend.db.entity.TransactionEntity;
import party.zonarius.pefibackend.db.repository.TransactionRepository;
import party.zonarius.pefibackend.script.upload.ScriptUploadRequest;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class ScriptControllerTest {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private WebApplicationContext ctx;

    @BeforeEach
    void setUp() {
        given()
            .webAppContextSetup(ctx)
            .multiPart("file", TestResources.Csv.exampleAsFile())
            .when()
            .post("/api/uploadCsv")
            .then()
            .statusCode(200);
    }

    @Test
    void constantTag() {
        uploadScript("constTag.js");

        List<TransactionEntity> txs = transactionRepository.findAll();
        assertThat(txs).extracting(TransactionEntity::getTag).allSatisfy(tag -> {
            assertThat(tag).isEqualTo("essen.geschäft.billa");
        });
    }

    private void uploadScript(String scriptName) {
        given().webAppContextSetup(ctx)
            .contentType("application/json")
            .body(ScriptUploadRequest.builder().script(TestUtil.getScript(scriptName)).build())
            .when()
            .post("/api/script")
            .then()
            .statusCode(200);
    }
}