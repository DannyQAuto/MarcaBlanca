package apis.coverage;

import models.coverage.request.CoverageRequest;
import models.coverage.response.CoverageResponse;
import utilities.BaseTest;
import utilities.Logs;
import utilities.TokenManager;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class CoverageTest extends BaseTest {

    private CoverageRequest coverageRequest;
    private static final String COVERAGE_PATH = "/api/v1/coverage/polygon";

    @BeforeClass
    public void setupCoverageTest() {
        coverageRequest = CoverageRequest.builder()
                .latitud(-12.211893244818599)
                .longitud(-77.00200435008979)
                .build();
    }

    @Test(groups = {"smoke", "coverage"})
    public void testObtenerCoberturaConToken() {
        Logs.info("\n========================================");
        Logs.info("📍 TEST: OBTENER COBERTURA");
        Logs.info("========================================");

        // 1. Obtener token
        String token = TokenManager.getKongAccessToken();

        // 2. Ejecutar request (el RequestFilter imprime todo: request, headers, body, response)
        Response response = given()
                .spec(buildKongPublicSpec())
                .header("Authorization", "Bearer " + token)
                .queryParam("latitud", coverageRequest.getLatitud())
                .queryParam("longitud", coverageRequest.getLongitud())
                .basePath(COVERAGE_PATH)
                .when()
                .get();

        // 3. Validar status code
        Assert.assertEquals(response.getStatusCode(), 200, "Status code debe ser 200");

        // 4. Convertir a POJO
        CoverageResponse coverageResponse = response.as(CoverageResponse.class);

        // 5. Validaciones
        Assert.assertEquals(coverageResponse.getStatus(), 0, "Status debe ser 0 (éxito)");
        Assert.assertEquals(coverageResponse.getMessage(), "Success", "Message debe ser 'Success'");
        Assert.assertNotNull(coverageResponse.getData(), "Data no debe ser nulo");
        Assert.assertNotNull(coverageResponse.getData().getEstado(), "Estado no debe ser nulo");

        Logs.info("✅ TEST PASADO EXITOSAMENTE");
    }
}