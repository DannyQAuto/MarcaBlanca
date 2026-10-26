package apis.NetworkEquipment.Activations;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.response.Response;
import models.ontActivations.request.OntActivationRequest;
import models.ontActivations.response.OntActivationResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;
import utilities.TokenManager;

import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * ============================================================
 *  TEST: Network Equipment / ONT Activations
 * ============================================================
 *  POST /api/v1/network-equipment/ont/activations
 *
 *  Headers:
 *      Accept        : application/json
 *      X-Channel     : WIN
 *      Authorization : Bearer {{auth_secret_1983}}
 * ============================================================
 */
public class NetworkEquipmentOntActivationsTest extends BaseTest {

    private static final String PATH = "/api/v1/network-equipment/ont/activations";

    private static final int CONNECT_TIMEOUT_MS = 60_000;
    private static final int SOCKET_TIMEOUT_MS  = 120_000;

    @Test(groups = {"smoke", "network", "ont", "activation"})
    public void testActivarOnt() {
        Logs.info("📍 TEST: ONT ACTIVATION — POST /ont/activations");

        // ---------- 1) Token ----------
        String token = TokenManager.getKongAccessToken();

        // ---------- 2) Body (usa el builder) ----------
        OntActivationRequest body = OntActivationRequest.builder()
                .ctoPower(-20.0)
                .orderNumber("2219366")
                .serialNumber("4857544347D796AC")
                .planCode("WIN_1GBPS_PLUS")
                .vlan("10")
                .zone("WIN")
                .fonowinEnabled(true)
                .ontVas("VAS-FREESWITCH")
                .fonowinVas("VAS-FREESWITCH")
                .productIds(List.of(1001, 200, 999999))
                .crmPlanCode(2880)
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
                .body(body)              // ← RestAssured serializa el objeto a JSON
                .when()
                .post();

        long elapsed = System.currentTimeMillis() - start;
        Logs.info("⏱ Tiempo de respuesta: " + elapsed + " ms");

        // ---------- 4) HTTP status ----------
        Assert.assertEquals(response.getStatusCode(), 200,
                "El endpoint debe responder 200");

        // ---------- 5) Deserializar y validar ----------
        OntActivationResponse res = response.as(OntActivationResponse.class);

        Logs.info(String.format("   status=%d, message=%s, data=%s",
                res.getStatus(),
                res.getMessage(),
                res.getData()));

        Assert.assertTrue(res.isExitoso(), "isExitoso() debe ser true (status=0)");

        Logs.info("✅ TEST PASADO");
    }
}