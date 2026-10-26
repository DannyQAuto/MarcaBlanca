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

public class ReconnectionsTest extends BaseTest {

    private static final String RECONNECTIONS_PATH =
            "/api/v1/bulk/services/reconnections";

    private BulkRequest reconnectionRequest;

    @BeforeClass
    public void setupRequest() {
        // 👇 Ahora es ReconnectionRequest.Order en vez de ReconnectionOrder
        BulkRequest.Order order = BulkRequest.Order.builder()
                .orderCode("1902627")
                .serialNumber("485754432EC0E6B0")
                .build();

        reconnectionRequest = BulkRequest.builder()
                .processType("APC")
                .orders(List.of(order))
                .build();

        Logs.info("Request preparado: " + reconnectionRequest);
    }

    @Test(groups = {regression})
    public void validarReconnection() {
        Logs.info("POST " + RECONNECTIONS_PATH);

        response = given()
                .spec(buildKongRequestSpec())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(reconnectionRequest)
                .when()
                .post(RECONNECTIONS_PATH)
                .then()
                .extract()
                .response();

        Logs.info("Status code: " + response.getStatusCode());
        Logs.info("Body:\n" + response.asPrettyString());

        Assert.assertEquals(response.getStatusCode(), 200, "Status HTTP inesperado");

        BulkResponse reconnection = response.as(BulkResponse.class);
        Assert.assertNotNull(reconnection, "No se pudo deserializar la respuesta");
        Assert.assertNotNull(reconnection.getStatus(), "status es null");
        Assert.assertNotNull(reconnection.getMessage(), "message es null");

        Logs.info("Reconnection validado: " + reconnection);
    }
}