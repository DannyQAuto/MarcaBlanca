package apis.contextualizacionont;

import io.restassured.response.Response;
import models.contextualization.response.PhysicalMacResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;

import static io.restassured.RestAssured.given;

public class PhysicalMacTest extends BaseTest {

    private static final String MAC_ROUTER = "485754437C592AA3";

    private static final String PHYSICAL_MAC_PATH =
            "/api/v1/contextualization/onts/" + MAC_ROUTER + "/physical-mac";

    @Test(groups = {regression})
    public void validarPhysicalMac() {
        Logs.info("GET " + PHYSICAL_MAC_PATH);

        response = given()
                .spec(buildKongRequestSpec())
                .when()
                .get(PHYSICAL_MAC_PATH)
                .then()
                .extract()
                .response();

        Logs.info("Status code: " + response.getStatusCode());
        Logs.info("Body:\n" + response.asPrettyString());

        Assert.assertEquals(response.getStatusCode(), 200, "Status HTTP inesperado");

        PhysicalMacResponse physicalMac = response.as(PhysicalMacResponse.class);

        Assert.assertNotNull(physicalMac, "No se pudo deserializar la respuesta");
        Assert.assertEquals(physicalMac.getStatus(), 0, "status lógico distinto de 0");
        Assert.assertNotNull(physicalMac.getMessage(), "message es null");
        Assert.assertNotNull(physicalMac.getData(), "data es null");

        Logs.info("PhysicalMac validado: " + physicalMac);
    }
}