package apis.auth;

import models.auth.request.TokenRequest;
import models.auth.response.TokenResponse;
import utilities.BaseTest;
import utilities.TokenManager;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class KongAuthTest extends BaseTest {

    @Test(groups = {"smoke", "auth", "kong"})
    public void testKongObtenerTokenExitoso() {
        // 1. Crear request con POJO
        TokenRequest request = TokenRequest.builder()
                .grantType("client_credentials")
                .clientId("ieKJneRZVVKdm9YHz4JkVzoCsRUdqIoK")
                .clientSecret("MA7CfFdZ738EBaBUXvGvnfipcy9oxA30")
                .build();

        System.out.println("\n=== [KONG] Test Obtener Token ===");
        System.out.println("Request: " + request);

        // 2. Ejecutar y validar
        TokenResponse tokenResponse = given()
                .spec(buildKongPublicSpec())  // ✅ Ya existe en BaseTest
                .body(request)
                .when()
                .post("/oauth2/token")
                .then()
                .statusCode(200)
                .log().ifValidationFails()
                .extract()
                .as(TokenResponse.class);

        // 3. Validar con POJO
        System.out.println("\n=== Response ===");
        System.out.println("Token: " + tokenResponse.getAccess_token().substring(0, 15) + "...");
        System.out.println("Expira en: " + tokenResponse.getExpires_in() + " segundos");
        System.out.println("Tipo: " + tokenResponse.getToken_type());

        Assert.assertNotNull(tokenResponse.getAccess_token(), "Access token no debe ser nulo");
        Assert.assertEquals(tokenResponse.getToken_type(), "bearer", "Token type debe ser bearer");
        Assert.assertEquals(tokenResponse.getExpires_in(), 7200, "Expiración debe ser 7200 segundos");
    }

    @Test(groups = {"auth", "kong"})
    public void testKongTokenManager() {
        // Usar TokenManager que maneja caché automáticamente
        String token1 = TokenManager.getKongAccessToken();
        String token2 = TokenManager.getKongAccessToken();

        // Debería ser el mismo token (reutilizado)
        Assert.assertEquals(token1, token2, "TokenManager debería reutilizar el token");

        System.out.println("✅ [KONG] Token Manager funcionando correctamente");
        System.out.println("Token: " + token1.substring(0, 15) + "...");
    }

    @Test(groups = {"auth", "kong"})
    public void testKongTokenEnMBPlanta() {
        // Cambiar a entorno Kong con autenticación
        switchToKongMBPlantaEnvironment();  // ✅ Ya existe en BaseTest

        // Hacer una petición a MBPlanta con token Kong
        Response response = request
                .basePath("/activacionvno")
                .body("{\"test\": \"data\"}")
                .post();

        System.out.println("Response status: " + response.getStatusCode());
        // Validar según lo que esperes de tu API
    }
}