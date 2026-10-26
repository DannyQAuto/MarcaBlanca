package apis.contextualizacionont;

import io.restassured.response.Response;
import models.contextualization.response.ServiceStatusResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;

import static io.restassured.RestAssured.given;

public class ServiceStatusTest extends BaseTest {

    private static final String MAC_ROUTER = "485754437C592AA3";

    private static final String SERVICE_STATUS_PATH =
            "/api/v1/contextualization/onts/" + MAC_ROUTER + "/service-status";

    @Test(groups = {regression})
    public void validarServiceStatus() {
        Logs.info("GET " + SERVICE_STATUS_PATH);

        response = given()
                .spec(buildKongRequestSpec())
                .when()
                .get(SERVICE_STATUS_PATH)
                .then()
                .extract()
                .response();

        Logs.info("Status code: " + response.getStatusCode());
        Logs.info("Body:\n" + response.asPrettyString());

        Assert.assertEquals(response.getStatusCode(), 200, "Status HTTP inesperado");

        ServiceStatusResponse serviceStatus = response.as(ServiceStatusResponse.class);

        Assert.assertNotNull(serviceStatus, "No se pudo deserializar la respuesta");
        Assert.assertEquals(serviceStatus.getStatus(), 0, "status lógico distinto de 0");
        Assert.assertNotNull(serviceStatus.getMessage(), "message es null");
        Assert.assertNotNull(serviceStatus.getData(), "data es null");

        Logs.info("ServiceStatus validado: " + serviceStatus);
    }
}