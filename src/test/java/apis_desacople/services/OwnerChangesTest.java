package apis.services;

import io.restassured.http.ContentType;
import models.services.request.OwnerChangeRequest;
import models.services.response.OwnerChangeResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;

import static io.restassured.RestAssured.given;

public class OwnerChangesTest extends BaseTest {

    private static final String OWNER_CHANGES_PATH =
            "/api/v1/services/owner-changes";

    private static final int ORDER_CODE = 396333;
    private static final int NEW_ORDER_CODE = 3123242;

    private OwnerChangeRequest ownerChangeRequest;

    @BeforeClass
    public void setupRequest() {
        ownerChangeRequest = OwnerChangeRequest.builder()
                .orderCode(ORDER_CODE)
                .newOrderCode(NEW_ORDER_CODE)
                .build();

        Logs.info("Request preparado: " + ownerChangeRequest);
    }

    @Test(groups = {regression})
    public void validarOwnerChange() {
        Logs.info("POST " + OWNER_CHANGES_PATH);

        response = given()
                .spec(buildKongRequestSpec())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(ownerChangeRequest)
                .when()
                .post(OWNER_CHANGES_PATH)
                .then()
                .extract()
                .response();

        Logs.info("Status code: " + response.getStatusCode());
        Logs.info("Body:\n" + response.asPrettyString());

        // ===== HTTP =====
        Assert.assertEquals(response.getStatusCode(), 200, "Status HTTP inesperado");

        // ===== Deserialización =====
        OwnerChangeResponse ownerChange = response.as(OwnerChangeResponse.class);
        Assert.assertNotNull(ownerChange, "No se pudo deserializar la respuesta");

        // ===== Nivel raíz =====
        Assert.assertEquals(ownerChange.getStatus(), 0, "Status lógico distinto de 0");
        Assert.assertEquals(ownerChange.getMessage(),
                "Cambio de titularidad realizado correctamente",
                "message inesperado");
        Assert.assertNotNull(ownerChange.getData(), "data es null");

        // ===== Nivel data =====
        OwnerChangeResponse.Data data = ownerChange.getData();

        Assert.assertEquals(data.getIdServicio(), NEW_ORDER_CODE,
                "id_servicio no coincide con newOrderCode");

        Logs.info("OwnerChange validado → id_servicio: " + data.getIdServicio());
    }
}