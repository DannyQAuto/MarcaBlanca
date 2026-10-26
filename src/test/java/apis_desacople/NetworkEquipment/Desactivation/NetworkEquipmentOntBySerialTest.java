package apis.NetworkEquipment.Desactivation;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.response.Response;
import models.ontSerial.response.OntSerialItem;
import models.ontSerial.response.OntSerialResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;
import utilities.TokenManager;

import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * ============================================================
 *  TEST: Network Equipment / ONT by Serial
 * ============================================================
 *  GET /api/v1/network-equipment/ont/485754432EC0E6B0
 *
 *  Headers:
 *      Accept        : application/json
 *      X-Channel     : WIN
 *      Authorization : Bearer {{auth_secret_1983}}
 * ============================================================
 */
public class NetworkEquipmentOntBySerialTest extends BaseTest {

    private static final String PATH = "/api/v1/network-equipment/ont/485754437C592AA3";

    private static final int CONNECT_TIMEOUT_MS = 60_000;
    private static final int SOCKET_TIMEOUT_MS  = 120_000;

    @Test(groups = {"smoke", "network", "ont"})
    public void testObtenerOntPorSerial() {
        Logs.info("📍 TEST: ONT BY SERIAL — Obtener y validar");

        // ---------- 1) Token ----------
        String token = TokenManager.getKongAccessToken();

        // ---------- 2) Llamada al endpoint ----------
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
                .when()
                .get();

        long elapsed = System.currentTimeMillis() - start;
        Logs.info("⏱ Tiempo de respuesta: " + elapsed + " ms");

        // ---------- 3) HTTP status ----------
        Assert.assertEquals(response.getStatusCode(), 200,
                "El endpoint debe responder 200");

        // ---------- 4) Deserialización ----------
        OntSerialResponse res = response.as(OntSerialResponse.class);

        Logs.info(String.format("   status=%d, message=%s, data.size=%s",
                res.getStatus(),
                res.getMessage(),
                res.getData() == null ? "null" : res.getData().size()));

        // ---------- 5) Validar wrapper ----------
        Assert.assertTrue(res.isExitoso(), "isExitoso() debe ser true (status=0)");
        Assert.assertNotNull(res.getData(), "data no debe ser null");
        Assert.assertFalse(res.getData().isEmpty(), "data no debe estar vacío");

        // ---------- 6) Validar campos del ONT ----------
        List<OntSerialItem> data = res.getData();
        OntSerialItem item = data.get(0);

        Logs.info("   olt_id           : " + item.getOltId());
        Logs.info("   olt_name         : " + item.getOltName());
        Logs.info("   modelo_router    : " + item.getModeloRouter());
        Logs.info("   plan_activado    : " + item.getPlanActivado());
        Logs.info("   potencia         : " + item.getPotencia());
        Logs.info("   potencia_olt     : " + item.getPotenciaOlt());

        Assert.assertNotNull(item.getOltId(), "olt_id no debe ser null");
        Assert.assertFalse(item.getOltId().isEmpty(), "olt_id no debe estar vacío");

        Assert.assertNotNull(item.getOltName(), "olt_name no debe ser null");
        Assert.assertFalse(item.getOltName().isEmpty(), "olt_name no debe estar vacío");

        Assert.assertNotNull(item.getModeloRouter(), "modelo_router no debe ser null");
        Assert.assertFalse(item.getModeloRouter().isEmpty(), "modelo_router no debe estar vacío");

        Assert.assertNotNull(item.getPlanActivado(), "plan_activado no debe ser null");

        Assert.assertNotNull(item.getPotencia(), "potencia no debe ser null");
        Assert.assertNotNull(item.getPotenciaOlt(), "potencia_olt no debe ser null");

        // Rango óptico típico GPON: -40 a 0 dBm
        Assert.assertTrue(item.getPotencia() >= -40 && item.getPotencia() <= 0,
                "potencia fuera de rango óptico: " + item.getPotencia());
        Assert.assertTrue(item.getPotenciaOlt() >= -40 && item.getPotenciaOlt() <= 0,
                "potencia_olt fuera de rango óptico: " + item.getPotenciaOlt());

        Logs.info("✅ TEST PASADO");
    }
}