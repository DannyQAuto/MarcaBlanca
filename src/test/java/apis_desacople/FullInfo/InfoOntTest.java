package apis.FullInfo;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.response.Response;
import models.FullInfo.response.InfoOntResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;
import utilities.TokenManager;

import static io.restassured.RestAssured.given;

/**
 * ============================================================
 *  TEST: Network Equipment / ONT Full Info
 * ============================================================
 *  GET /api/v1/network-equipment/ont/{serial}/full-info
 *
 *  Host    : 172.16.27.41:8080  (ya viene en buildKongPublicSpec)
 *  Headers:
 *      Accept        : application/json
 *      X-Channel     : WIN               (ya viene en el spec)
 *      Authorization : bearer {token}
 * ============================================================
 */
public class InfoOntTest extends BaseTest {

    private static final String SERIAL = "4857544347D796AC";
    private static final String PATH   = "/api/v1/network-equipment/ont/" + SERIAL + "/full-info";

    private static final int CONNECT_TIMEOUT_MS = 60_000;
    private static final int SOCKET_TIMEOUT_MS  = 120_000;

    @Test(groups = {"smoke", "network", "ont", "fullinfo"})
    public void testObtenerFullInfoOnt() {
        Logs.info("📍 TEST: ONT FULL INFO — GET /ont/" + SERIAL + "/full-info");

        // ---------- 1) Token ----------
        String token = TokenManager.getKongAccessToken();

        // ---------- 2) GET ----------
        long start = System.currentTimeMillis();

        Response response = given()
                .spec(buildKongPublicSpec())          // ← trae baseUri, X-Channel, Content-Type
                .config(RestAssured.config()
                        .httpClient(HttpClientConfig.httpClientConfig()
                                .setParam("http.connection.timeout", CONNECT_TIMEOUT_MS)
                                .setParam("http.socket.timeout", SOCKET_TIMEOUT_MS)))
                .header("Accept", "application/json")
                .header("Authorization", "bearer " + token)
                .basePath(PATH)
                .when()
                .get();

        long elapsed = System.currentTimeMillis() - start;
        Logs.info("⏱ Tiempo de respuesta: " + elapsed + " ms");

        // ---------- 3) HTTP status ----------
        Assert.assertEquals(response.getStatusCode(), 200,
                "El endpoint debe responder 200");

        // ---------- 4) Deserializar y validar ----------
        InfoOntResponse res = response.as(InfoOntResponse.class);

        Logs.info("📦 Response: " + res);

        Assert.assertNotNull(res, "La respuesta no debe ser null");

        Logs.info("✅ TEST PASADO");
    }
}