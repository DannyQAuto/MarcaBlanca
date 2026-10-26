package apis.contextualizacionont;

import io.restassured.response.Response;
import models.contextualization.response.CustomerServicesResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;

import static io.restassured.RestAssured.given;

public class
CustomerServicesTest extends BaseTest {

    private static final String CUSTOMER_SERVICES_PATH =
            "/api/v1/contextualization/customer-services/1743160";

    @Test(groups = {regression})
    public void validarCustomerServices() {
        Logs.info("GET " + CUSTOMER_SERVICES_PATH);

        // 👇 Igual que en BrandInfoTest: usa el spec de Kong de BaseTest
        response = given()
                .spec(buildKongRequestSpec())
                .when()
                .get(CUSTOMER_SERVICES_PATH)
                .then()
                .extract()
                .response();

        Logs.info("Status code: " + response.getStatusCode());
        Logs.info("Body:\n" + response.asPrettyString());

        Assert.assertEquals(response.getStatusCode(), 200, "Status HTTP inesperado");

        CustomerServicesResponse customerServices =
                response.as(CustomerServicesResponse.class);

        Assert.assertNotNull(customerServices, "No se pudo deserializar la respuesta");

        // Ajusta estas validaciones según el JSON real:
        Assert.assertEquals(customerServices.getStatus(), 0, "status lógico distinto de 0");
        Assert.assertNotNull(customerServices.getData(), "data es null");

        Logs.info("CustomerServices validado: " + customerServices);
    }
}