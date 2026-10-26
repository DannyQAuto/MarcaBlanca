package apis.bulk;

import io.restassured.http.ContentType;
import models.bulk.request.BulkRequest;
import models.bulk.response.BulkResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;

import java.util.List;

import static io.restassured.RestAssured.given;

public class SuspensionsTest extends BaseTest {

    private static final String SUSPENSIONS_PATH =
            "/api/v1/bulk/services/suspensions";

    private static final String ORDER_CODE = "1902627";
    private static final String SERIAL_NUMBER = "485754432EC0E6B0";

    private BulkRequest suspensionRequest;

    @BeforeClass
    public void setupRequest() {
        BulkRequest.Order order = BulkRequest.Order.builder()
                .orderCode(ORDER_CODE)
                .serialNumber(SERIAL_NUMBER)
                .build();

        suspensionRequest = BulkRequest.builder()
                .processType("APC")
                .orders(List.of(order))
                .build();

        Logs.info("Request preparado: " + suspensionRequest);
    }

    @Test(groups = {regression})
    public void validarSuspension() {
        Logs.info("POST " + SUSPENSIONS_PATH);

        response = given()
                .spec(buildKongRequestSpec())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(suspensionRequest)
                .when()
                .post(SUSPENSIONS_PATH)
                .then()
                .extract()
                .response();

        Logs.info("Status code: " + response.getStatusCode());
        Logs.info("Body:\n" + response.asPrettyString());

        // ===== HTTP =====
        Assert.assertEquals(response.getStatusCode(), 200, "Status HTTP inesperado");

        // ===== Deserialización =====
        BulkResponse suspension = response.as(BulkResponse.class);
        Assert.assertNotNull(suspension, "No se pudo deserializar la respuesta");

        // ===== Nivel API =====
        Assert.assertEquals(suspension.getStatus(), 0,
                "Status API distinto de 0 (fallo de infraestructura)");
        Assert.assertNotNull(suspension.getData(), "data es null");
        Assert.assertFalse(suspension.getData().isEmpty(), "data está vacío");

        // ===== Nivel negocio (flexible) =====
        BulkResponse.Data data = suspension.getData().get(0);

        Assert.assertNotNull(data.getStatus(), "status del item es null");
        Assert.assertNotNull(data.getMsg(), "msg es null");

        // nserie y npedido deben coincidir con lo enviado
        Assert.assertEquals(data.getNserie(), SERIAL_NUMBER, "nserie no coincide");
        Assert.assertEquals(data.getNpedido(), ORDER_CODE, "npedido no coincide");

        // Log del resultado de negocio
        Logs.info("Resultado de negocio → status: " + data.getStatus()
                + " | msg: " + data.getMsg()
                + " | estado_new: " + data.getEstadoNew());

        // Status aceptado: 0 (éxito) o -1 (fallo controlado de negocio)
        Assert.assertTrue(
                data.getStatus().equals("0") || data.getStatus().equals("-1"),
                "status de negocio inesperado: " + data.getStatus()
        );

        Logs.info("Suspension validado: " + data);
    }
}