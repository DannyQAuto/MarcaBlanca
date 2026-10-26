package apis.NetworkSites;

import io.restassured.response.Response;
import models.network.response.NetworkSiteItem;
import models.network.response.NetworkSiteResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;
import utilities.TokenManager;

import static io.restassured.RestAssured.given;

/**
 * ============================================================
 *  TEST: Network Sites
 * ============================================================
 *  GET /api/v1/network-sites/{codigo}
 *  Ejemplo: /api/v1/network-sites/W-22SRE02-25
 *
 *  ✔ UNA SOLA llamada al endpoint en @BeforeClass.
 *  ✔ Los 3 tests comparten la respuesta cacheada.
 * ============================================================
 */
public class NetworkSiteTest extends BaseTest {

    private static final String PATH = "/api/v1/network-sites";
    private static final String CODIGO_BUSCADO = "W-22SRE02-25";

    // Cache — se llena UNA SOLA VEZ en @BeforeClass
    private Response responseCache;
    private NetworkSiteResponse resCache;

    // ============================================================
    // SETUP — ÚNICA LLAMADA AL ENDPOINT
    // ============================================================
    @BeforeClass
    public void setupAndCallOnce() {
        Logs.info("🔧 [SETUP] NetworkSiteTest — código: " + CODIGO_BUSCADO);

        String token = TokenManager.getKongAccessToken();

        responseCache = given()
                .spec(buildKongPublicSpec())
                .header("Authorization", "Bearer " + token)
                .basePath(PATH + "/" + CODIGO_BUSCADO)
                .when()
                .get();

        Assert.assertEquals(responseCache.getStatusCode(), 200,
                "El endpoint debe responder 200");

        resCache = responseCache.as(NetworkSiteResponse.class);
        Logs.info("💾 [SETUP] Respuesta cacheada. Los 3 tests la reutilizarán.");
    }

    // ============================================================
    // TEST 1: Wrapper
    // ============================================================
    @Test(groups = {"smoke", "site"})
    public void testObtenerNetworkSite() {
        Logs.info("📍 TEST 1: NETWORK SITES — Wrapper");

        Logs.info(String.format("   status=%d, message=%s, data.size=%d",
                resCache.getStatus(),
                resCache.getMessage(),
                resCache.getData() == null ? 0 : resCache.getData().size()));

        Assert.assertTrue(resCache.isExitoso(), "isExitoso() debe ser true");
        Assert.assertEquals(resCache.getMessage(), "Success");
        Assert.assertNotNull(resCache.getData(), "data no debe ser null");
        Assert.assertFalse(resCache.getData().isEmpty(), "data no debe estar vacío");

        Logs.info("✅ [TEST 1] PASADO");
    }

    // ============================================================
    // TEST 2: Validar campos de cada item
    // ============================================================
    @Test(groups = {"site"},
            dependsOnMethods = "testObtenerNetworkSite")
    public void testValidarCamposNetworkSite() {
        Logs.info("📍 TEST 2: NETWORK SITES — Validar campos");
        Logs.info("📋 Recorriendo " + resCache.getData().size() + " items");

        int index = 0;
        for (NetworkSiteItem item : resCache.getData()) {
            String ctx = "[item " + index + "]";

            Assert.assertNotNull(item.getId_caja(), ctx + " id_caja no debe ser null");
            Assert.assertNotNull(item.getCod_antiguo(), ctx + " cod_antiguo no debe ser null");
            Assert.assertNotNull(item.getCod_nuevo(), ctx + " cod_nuevo no debe ser null");
            Assert.assertNotNull(item.getTipo(), ctx + " tipo no debe ser null");
            Assert.assertNotNull(item.getPuertos(), ctx + " puertos no debe ser null");
            Assert.assertNotNull(item.getEstado(), ctx + " estado no debe ser null");

            Logs.info(String.format("   ✔ cod_nuevo=%s | cod_antiguo=%s | id_caja=%s | tipo=%s | puertos=%s | estado=%s",
                    item.getCod_nuevo(),
                    item.getCod_antiguo(),
                    item.getId_caja(),
                    item.getTipo(),
                    item.getPuertos(),
                    item.getEstado()));

            Logs.info(String.format("     new_codigo=%s | nodo=%s | celda=%s",
                    item.getNew_codigo(),
                    item.getNodo(),
                    item.getCelda()));

            Logs.info(String.format("     ubicación : %s / %s / %s",
                    item.getDistrito(),
                    item.getProvincia(),
                    item.getDepartamento()));

            Logs.info(String.format("     lat/lon   : %s / %s",
                    item.getLatitud(),
                    item.getLongitud()));

            Logs.info("     direccion : " + item.getDireccion());

            index++;
        }

        Logs.info("✅ [TEST 2] PASADO");
    }

    // ============================================================
    // TEST 3: Validar el código buscado específicamente
    // ============================================================
    @Test(groups = {"site", "data-specific"},
            dependsOnMethods = "testValidarCamposNetworkSite")
    public void testValidarCodigoBuscado() {
        Logs.info("📍 TEST 3: NETWORK SITES — Validar código buscado");
        Logs.info("🔎 Buscando: " + CODIGO_BUSCADO);

        NetworkSiteItem encontrado = null;
        for (NetworkSiteItem item : resCache.getData()) {
            if (CODIGO_BUSCADO.equals(item.getCod_antiguo())
                    || CODIGO_BUSCADO.equals(item.getCod_nuevo())) {
                encontrado = item;
                break;
            }
        }

        Assert.assertNotNull(encontrado, "Debe existir el código: " + CODIGO_BUSCADO);
        Assert.assertEquals(encontrado.getId_caja(), "00000603025");
        Assert.assertEquals(encontrado.getTipo(), "CTO");

        Logs.info("✅ Encontrado:");
        Logs.info("   cod_nuevo   : " + encontrado.getCod_nuevo());
        Logs.info("   cod_antiguo : " + encontrado.getCod_antiguo());
        Logs.info("   id_caja     : " + encontrado.getId_caja());
        Logs.info("   tipo        : " + encontrado.getTipo());
        Logs.info("   puertos     : " + encontrado.getPuertos());
        Logs.info("   estado      : " + encontrado.getEstado());

        Logs.info("✅ [TEST 3] PASADO");
    }
}