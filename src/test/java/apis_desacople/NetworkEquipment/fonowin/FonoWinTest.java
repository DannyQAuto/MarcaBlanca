package apis.NetworkEquipment.fonowin;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.response.Response;
import models.FonoWin.request.FonoWinNumberRequest;
import models.FonoWin.response.FonoWinNumberResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;
import utilities.TokenManager;

import static io.restassured.RestAssured.given;

/**
 * ============================================================
 *  TEST: Network Equipment / ONT FonoWIN Number
 * ============================================================
 *  POST /api/v1/network-equipment/ont/fonowin-number
 *
 *  Host    : 172.16.27.41:8080  (ya viene en buildKongPublicSpec)
 *  Headers:
 *      Content-Type  : application/json  (ya viene en el spec)
 *      X-Channel     : WIN               (ya viene en el spec)
 *      Authorization : bearer {token}
 * ============================================================
 */
public class FonoWinTest extends BaseTest {

    private static final String PATH = "/api/v1/network-equipment/ont/fonowin-number";

    private static final int CONNECT_TIMEOUT_MS = 60_000;
    private static final int SOCKET_TIMEOUT_MS  = 120_000;

    @Test(groups = {"smoke", "network", "ont", "fonowin"})
    public void testConfigurarFonowinNumber() {
        Logs.info("📍 TEST: ONT FONOWIN NUMBER — POST /ont/fonowin-number");

        // ---------- 1) Token ----------
        String token = TokenManager.getKongAccessToken();

        // ---------- 2) Body (usa el builder) ----------
        FonoWinNumberRequest body = FonoWinNumberRequest.builder()
                .serialNumber("485754437C592AA3")
                .sipUsername("43711812")
                .sipName("43711812")
                .build();

        Logs.info("📦 Body: " + body);

        // ---------- 3) POST ----------
        long start = System.currentTimeMillis();

        Response response = given()
                .spec(buildKongPublicSpec())          // ← trae baseUri, X-Channel, Content-Type
                .config(RestAssured.config()
                        .httpClient(HttpClientConfig.httpClientConfig()
                                .setParam("http.connection.timeout", CONNECT_TIMEOUT_MS)
                                .setParam("http.socket.timeout", SOCKET_TIMEOUT_MS)))
                .header("Authorization", "bearer " + token)
                .basePath(PATH)
                .body(body)
                .when()
                .post();

        long elapsed = System.currentTimeMillis() - start;
        Logs.info("⏱ Tiempo de respuesta: " + elapsed + " ms");

        // ---------- 4) HTTP status ----------
        Assert.assertEquals(response.getStatusCode(), 200,
                "El endpoint debe responder 200");

        // ---------- 5) Deserializar y validar ----------
        FonoWinNumberResponse res = response.as(FonoWinNumberResponse.class);

        Logs.info(String.format("   status=%d, message=%s, data=%s",
                res.getStatus(),
                res.getMessage(),
                res.getData()));

        Assert.assertTrue(res.isExitoso(), "isExitoso() debe ser true (status=0)");

        Logs.info("✅ TEST PASADO");
    }
}