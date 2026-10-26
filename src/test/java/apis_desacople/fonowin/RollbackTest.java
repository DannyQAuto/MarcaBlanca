package apis.fonowin;

import io.restassured.http.ContentType;
import models.fonowin.request.RollbackRequest;
import models.fonowin.response.RollbackResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;

import static io.restassured.RestAssured.given;

public class RollbackTest extends BaseTest {

    private static final String ROLLBACK_PATH =
            "/api/v1/fonowin/numbers/rollback";

    private RollbackRequest rollbackRequest;

    @BeforeClass
    public void setupRequest() {
        rollbackRequest = RollbackRequest.builder()
                .orderNumber(2888006)
                .phoneNumber("043711812")
                .sourceApplication("TELEGRAM")
                .portability(false)
                .freeSwitch(false)   // 👈 ajustado al valor real enviado
                .build();

        Logs.info("Request preparado: " + rollbackRequest);
    }

    @Test(groups = {regression})
    public void validarRollback() {
        Logs.info("POST " + ROLLBACK_PATH);

        response = given()
                .spec(buildKongRequestSpec())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(rollbackRequest)
                .when()
                .post(ROLLBACK_PATH)
                .then()
                .extract()
                .response();

        Logs.info("Status code: " + response.getStatusCode());
        Logs.info("Body:\n" + response.asPrettyString());

        // ===== Validación HTTP =====
        Assert.assertEquals(response.getStatusCode(), 200, "Status HTTP inesperado");

        // ===== Deserialización =====
        RollbackResponse rollback = response.as(RollbackResponse.class);
        Assert.assertNotNull(rollback, "No se pudo deserializar la respuesta");
        Assert.assertNotNull(rollback.getMsg(), "msg es null");

        // ===== Validaciones del body =====
        RollbackResponse.Msg msg = rollback.getMsg();

        // status == 1 es el indicador de éxito en Fonowin
        Assert.assertEquals(msg.getStatus(), 1, "Status lógico distinto de 1");

        // resultado no es un valor fijo, pero sí debe existir
        Assert.assertNotNull(msg.getResultado(), "resultado es null");
        Assert.assertFalse(msg.getResultado().isEmpty(), "resultado está vacío");

        // numero debe existir (puede ser "0")
        Assert.assertNotNull(msg.getNumero(), "numero es null");

        Logs.info("Rollback validado: " + rollback);
    }
}