package apis.contextualizacionont;

import io.restassured.response.Response;
import models.contextualization.response.BrandInfoResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;

import static io.restassured.RestAssured.given;

public class BrandInfoTest extends BaseTest {

    private static final String BRAND_INFO_PATH =
            "/api/v1/contextualization/onts/48575443DD32AFA6/brand-info";

    @Test(groups = {regression})
    public void validarBrandInfo() {
        Logs.info("GET " + BRAND_INFO_PATH);

        // 👇 Usa el spec de Kong que ya tienes en BaseTest
        response = given()
                .spec(buildKongRequestSpec())
                .when()
                .get(BRAND_INFO_PATH)
                .then()
                .extract()
                .response();

        Logs.info("Status code: " + response.getStatusCode());
        Logs.info("Body:\n" + response.asPrettyString());

        Assert.assertEquals(response.getStatusCode(), 200, "Status HTTP inesperado");

        BrandInfoResponse brandInfo = response.as(BrandInfoResponse.class);

        Assert.assertNotNull(brandInfo, "No se pudo deserializar la respuesta");
        Assert.assertEquals(brandInfo.getStatus(), 0, "status lógico distinto de 0");
        Assert.assertEquals(brandInfo.getMessage(),
                "Información de marca obtenida exitosamente");
        Assert.assertNotNull(brandInfo.getData(), "data es null");
        Assert.assertFalse(brandInfo.getData().isEmpty(), "data está vacío");

        BrandInfoResponse.Data data = brandInfo.getData().get(0);
        Assert.assertEquals(data.getDescMarca(), "Huawei");
        Assert.assertEquals(data.getDescPrefijo(), "HWTC");
        Assert.assertEquals(data.getDescPrefijoActivacion(), "48575443");
        Assert.assertEquals(data.getMacPrefijoActivacion(), "48575443DD32AFA6");
        Assert.assertEquals(data.getMacPrefijoMarca(), "HWTCDD32AFA6");
        Assert.assertEquals(data.getMacRouter(), "48575443DD32AFA6");

        Logs.info("BrandInfo validado: " + data);
    }
}