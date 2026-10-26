package apis_desacople.fonowin;

import io.restassured.http.ContentType;
import models.fonowin.request.PortabilityRequest;
import models.fonowin.response.PortabilityResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;
import utilities.NcReader;              // 👈 NUEVO

import static io.restassured.RestAssured.given;

public class PortabilitiesTest extends BaseTest {

    private static final String PORTABILITIES_PATH =
            "/api/v1/fonowin/numbers/portabilities";

    private PortabilityRequest portabilityRequest;

    @BeforeClass
    public void setupRequest() {

        // ---------- Leer MAC desde nc.json ----------
        String macAddress = NcReader.getNc();
        Logs.info("🔑 macAddress leído desde nc.json: " + macAddress);

        portabilityRequest = PortabilityRequest.builder()
                .processCode("pr-phonewin-activacion")
                .processVersion("1")
                .taskCode("Activity_12a888")
                .outputCode("REGISTRAR_PHONEWIN")
                .taskObservations("")
                .taskGroupFile("")
                .orderNumber(3088019)
                .customerName("")
                .address("")
                .ctoLatitude(0)
                .ctoLongitude(0)
                .macAddress(macAddress)         // 👈 viene del JSON
                .fonowinNumber("4550260")
                .portability(true)
                .build();

        Logs.info("Request preparado: " + portabilityRequest);
    }

    @Test(groups = {regression})
    public void validarPortability() {
        Logs.info("POST " + PORTABILITIES_PATH);

        response = given()
                .spec(buildKongRequestSpec())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(portabilityRequest)
                .when()
                .post(PORTABILITIES_PATH)
                .then()
                .extract()
                .response();

        Logs.info("Status code: " + response.getStatusCode());
        Logs.info("Body:\n" + response.asPrettyString());

        Assert.assertEquals(response.getStatusCode(), 200, "Status HTTP inesperado");

        PortabilityResponse portability = response.as(PortabilityResponse.class);

        Assert.assertNotNull(portability, "No se pudo deserializar la respuesta");
        Assert.assertNotNull(portability.getStatus(), "status es null");
        Assert.assertNotNull(portability.getMessage(), "message es null");

        Logs.info("Portability validado: " + portability);
    }
}