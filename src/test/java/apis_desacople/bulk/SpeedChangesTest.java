package apis.bulk;

import io.restassured.http.ContentType;
import models.bulk.request.SpeedChangeRequest;
import models.bulk.response.BulkResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;

import static io.restassured.RestAssured.given;

public class SpeedChangesTest extends BaseTest {

    private static final String SPEED_CHANGES_PATH =
            "/api/v1/bulk/services/speed-changes";

    private static final String GROUP = "WIN";
    private static final String SPEED = "1000";
    private static final String SERIAL_NUMBER = "48575443FD8875AE";
    private static final long ORDER_CODE = 333333333334L;   // 👈 L al final (long)

    private SpeedChangeRequest speedChangeRequest;

    @BeforeClass
    public void setupRequest() {
        speedChangeRequest = SpeedChangeRequest.builder()
                .group(GROUP)
                .speed(SPEED)
                .serialNumber(SERIAL_NUMBER)
                .orderCode(ORDER_CODE)
                .build();

        Logs.info("Request preparado: " + speedChangeRequest);
    }

    @Test(groups = {regression})
    public void validarSpeedChange() {
        Logs.info("POST " + SPEED_CHANGES_PATH);

        response = given()
                .spec(buildKongRequestSpec())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(speedChangeRequest)
                .when()
                .post(SPEED_CHANGES_PATH)
                .then()
                .extract()
                .response();

        Logs.info("Status code: " + response.getStatusCode());
        Logs.info("Body:\n" + response.asPrettyString());

        // ===== HTTP =====
        Assert.assertEquals(response.getStatusCode(), 200, "Status HTTP inesperado");

        // ===== Deserialización (asumiendo shape similar a bulk) =====
        BulkResponse speedChange = response.as(BulkResponse.class);
        Assert.assertNotNull(speedChange, "No se pudo deserializar la respuesta");

        // ===== Nivel API =====
        Assert.assertEquals(speedChange.getStatus(), 0,
                "Status API distinto de 0 (fallo de infraestructura)");
        Assert.assertNotNull(speedChange.getData(), "data es null");
        Assert.assertFalse(speedChange.getData().isEmpty(), "data está vacío");

        // ===== Nivel negocio (flexible) =====
        BulkResponse.Data data = speedChange.getData().get(0);

        Assert.assertNotNull(data.getStatus(), "status del item es null");
        Assert.assertNotNull(data.getMsg(), "msg es null");

        Logs.info("Resultado de negocio → status: " + data.getStatus()
                + " | msg: " + data.getMsg()
                + " | estado_new: " + data.getEstadoNew());

        Assert.assertTrue(
                data.getStatus().equals("0") || data.getStatus().equals("-1"),
                "status de negocio inesperado: " + data.getStatus()
        );

        Logs.info("SpeedChange validado: " + data);
    }
}