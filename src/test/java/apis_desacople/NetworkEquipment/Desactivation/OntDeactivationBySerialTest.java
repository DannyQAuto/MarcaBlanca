package apis.networkequipment;

import io.restassured.http.ContentType;
import models.networkequipment.response.OntDeactivationResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;

import static io.restassured.RestAssured.given;

public class OntDeactivationBySerialTest extends BaseTest {

    private static final String SERIAL_NUMBER = "4857544347D796AC";

    private static final String DEACTIVATION_PATH =
            "/api/v1/network-equipment/ont/" + SERIAL_NUMBER + "/deactivation";

    @Test(groups = {regression})
    public void validarOntDeactivationBySerial() {
        Logs.info("POST " + DEACTIVATION_PATH);

        response = given()
                .spec(buildKongRequestSpec())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post(DEACTIVATION_PATH)   // 👈 sin .body()
                .then()
                .extract()
                .response();

        Logs.info("Status code: " + response.getStatusCode());
        Logs.info("Body:\n" + response.asPrettyString());

        // ===== HTTP =====
        Assert.assertEquals(response.getStatusCode(), 200, "Status HTTP inesperado");

        // ===== Deserialización =====
        OntDeactivationResponse deactivation =
                response.as(OntDeactivationResponse.class);
        Assert.assertNotNull(deactivation, "No se pudo deserializar la respuesta");

        // ===== Validaciones flexibles (hasta ver JSON real) =====
        Assert.assertNotNull(deactivation.getStatus(), "status es null");
        Logs.info("Status lógico: " + deactivation.getStatus()
                + " | message: " + deactivation.getMessage());

        Logs.info("OntDeactivationBySerial validado: " + deactivation);
    }
}