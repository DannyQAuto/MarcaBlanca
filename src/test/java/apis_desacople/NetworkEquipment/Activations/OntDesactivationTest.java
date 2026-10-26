package apis.networkequipment;

import io.restassured.http.ContentType;
import models.networkequipment.request.OntDeactivationRequest;
import models.networkequipment.response.OntDeactivationResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;

import static io.restassured.RestAssured.given;

public class OntDesactivationTest extends BaseTest {

    private static final String ACTIVATION_ID = "1791930513967";

    private static final String DEACTIVATION_PATH =
            "/api/v1/network-equipment/ont/activations/"
                    + ACTIVATION_ID + "/deactivation";

    private static final String SERIAL_NUMBER = "4857544347D796AC";

    private OntDeactivationRequest deactivationRequest;

    @BeforeClass
    public void setupRequest() {
        deactivationRequest = OntDeactivationRequest.builder()
                .serialNumber(SERIAL_NUMBER)
                .build();

        Logs.info("Request preparado: " + deactivationRequest);
    }

    @Test(groups = {regression})
    public void validarOntDeactivation() {
        Logs.info("POST " + DEACTIVATION_PATH);

        response = given()
                .spec(buildKongRequestSpec())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(deactivationRequest)
                .when()
                .post(DEACTIVATION_PATH)
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

        Logs.info("OntDeactivation validado: " + deactivation);
    }
}