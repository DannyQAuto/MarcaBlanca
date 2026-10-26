package apis.NetworkEquipment.vas;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.response.Response;
import models.ontVas.request.OntVasRequest;
import models.ontVas.response.OntVasResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;
import utilities.TokenManager;

import static io.restassured.RestAssured.given;

/**
 * ============================================================
 *  TEST: Network Equipment / ONT VAS
 * ============================================================
 *  POST /api/v1/network-equipment/ont/485754437C592AA3/vas
 *
 *  Headers:
 *      Accept        : application/json
 *      X-Channel     : WIN
 *      Authorization : Bearer {{auth_secret_1983}}
 *
 *  Body:
 *      { "valueAddedService": "VAS_WINTOTAL_IPFIJA" }
 * ============================================================
 */
public class NetworkEquipmentOntVasTest extends BaseTest {

    private static final String SERIAL = "485754437C592AA3";
    private static final String PATH = "/api/v1/network-equipment/ont/" + SERIAL + "/vas";

    private static final int CONNECT_TIMEOUT_MS = 60_000;
    private static final int SOCKET_TIMEOUT_MS  = 120_000;

    @Test(groups = {"smoke", "network", "ont", "vas"})
    public void testAgregarVasAlOnt() {
        Logs.info("📍 TEST: ONT VAS — POST /ont/" + SERIAL + "/vas");

        // ---------- 1) Token ----------
        String token = TokenManager.getKongAccessToken();

        // ---------- 2) Body ----------
        OntVasRequest body = OntVasRequest.builder()
                .valueAddedService("VAS_WINTOTAL_IPFIJA")
                .build();

        Logs.info("📦 Body: " + body);

        // ---------- 3) POST ----------
        long start = System.currentTimeMillis();

        Response response = given()
                .spec(buildKongPublicSpec())
                .config(RestAssured.config()
                        .httpClient(HttpClientConfig.httpClientConfig()
                                .setParam("http.connection.timeout", CONNECT_TIMEOUT_MS)
                                .setParam("http.socket.timeout", SOCKET_TIMEOUT_MS)))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
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
        OntVasResponse res = response.as(OntVasResponse.class);

        Logs.info(String.format("   status=%d, message=%s, data=%s",
                res.getStatus(),
                res.getMessage(),
                res.getData()));

        Assert.assertTrue(res.isExitoso(), "isExitoso() debe ser true (status=0)");

        Logs.info("✅ TEST PASADO");
    }
}