package apis.NetworkEquipment.brands;

import io.restassured.response.Response;
import models.network.response.OntBrandItem;
import models.network.response.OntBrandsResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;
import utilities.TokenManager;

import static io.restassured.RestAssured.given;

/**
 * ============================================================
 *  TEST: Network Equipment / ONT Brands
 * ============================================================
 *  GET /api/v1/network-equipment/ont/brands
 *
 *  ✔ UNA SOLA llamada al endpoint en @BeforeClass.
 *  ✔ Los 3 tests comparten la respuesta cacheada.
 *  ✔ Cero riesgo de HTTP 429 (Too Many Requests).
 * ============================================================
 */
public class NetworkEquipmentOntBrandsTest extends BaseTest {

    private static final String PATH = "/api/v1/network-equipment/ont/brands";

    // Cache — se llena UNA SOLA VEZ en @BeforeClass
    private Response responseCache;
    private OntBrandsResponse resCache;

    // ============================================================
    // SETUP — ÚNICA LLAMADA AL ENDPOINT
    // ============================================================
    @BeforeClass
    public void setupAndCallOnce() {
        Logs.info("🔧 [SETUP] NetworkEquipmentOntBrandsTest — única llamada al endpoint");

        // El RequestFilter ya imprime URL, headers, body, status y response headers.
        // Aquí solo hacemos la llamada y validamos el status.
        String token = TokenManager.getKongAccessToken();

        responseCache = given()
                .spec(buildKongPublicSpec())
                .header("Authorization", "Bearer " + token)
                .basePath(PATH)
                .when()
                .get();

        Assert.assertEquals(responseCache.getStatusCode(), 200,
                "El endpoint debe responder 200");

        resCache = responseCache.as(OntBrandsResponse.class);
        Logs.info("💾 [SETUP] Respuesta cacheada. Los 3 tests la reutilizarán.");
    }

    // ============================================================
    // TEST 1 — Wrapper
    // ============================================================
    @Test(groups = {"smoke", "network", "ont"})
    public void testObtenerOntBrands() {
        Logs.info("📍 TEST 1: ONT BRANDS — Wrapper");

        Logs.info(String.format("   status=%d, message=%s, data.size=%s",
                resCache.getStatus(),
                resCache.getMessage(),
                resCache.getData() == null ? "null" : resCache.getData().size()));

        Assert.assertTrue(resCache.isExitoso(), "El flag isExitoso() debe ser true");
        Assert.assertEquals(resCache.getMessage(), "Marca obtenida correctamente");
        Assert.assertNotNull(resCache.getData(), "data no debe ser null");
        Assert.assertFalse(resCache.getData().isEmpty(), "data no debe estar vacío");

        Logs.info("✅ [TEST 1] PASADO");
    }

    // ============================================================
    // TEST 2 — Validar cada marca
    // ============================================================
    @Test(groups = {"network", "ont"},
            dependsOnMethods = "testObtenerOntBrands")
    public void testValidarCadaMarca() {
        Logs.info("📍 TEST 2: ONT BRANDS — Validar cada marca");
        Logs.info("📋 Recorriendo " + resCache.getData().size() + " marcas");

        for (OntBrandItem item : resCache.getData()) {
            Assert.assertNotNull(item.getDesc_marca(), "desc_marca no debe ser null");
            Assert.assertNotNull(item.getDesc_prefijo(), "desc_prefijo no debe ser null");
            Assert.assertNotNull(item.getDesc_prefijo_activacion(), "desc_prefijo_activacion no debe ser null");
            Assert.assertFalse(item.getDesc_marca().isEmpty(), "desc_marca no debe estar vacío");
            Assert.assertFalse(item.getDesc_prefijo().isEmpty(), "desc_prefijo no debe estar vacío");

            Logs.info(String.format("   ✔ %s | prefijo: %s | prefijo_activacion: %s",
                    item.getDesc_marca(),
                    item.getDesc_prefijo(),
                    item.getDesc_prefijo_activacion()));
        }

        Logs.info("✅ [TEST 2] PASADO");
    }

    // ============================================================
    // TEST 3 — Buscar una marca específica
    // ============================================================
    @Test(groups = {"network", "ont"},
            dependsOnMethods = "testValidarCadaMarca")
    public void testBuscarMarcaEspecifica() {
        Logs.info("📍 TEST 3: ONT BRANDS — Buscar marca específica");

        String marcaBuscada = "Huawei";
        Logs.info("🔎 Buscando marca: " + marcaBuscada);

        OntBrandItem encontrado = null;
        for (OntBrandItem item : resCache.getData()) {
            if (marcaBuscada.equals(item.getDesc_marca())) {
                encontrado = item;
                break;
            }
        }

        Assert.assertNotNull(encontrado, "Debe existir la marca: " + marcaBuscada);
        Assert.assertEquals(encontrado.getDesc_prefijo(), "HWTC");
        Assert.assertEquals(encontrado.getDesc_prefijo_activacion(), "48575443");

        Logs.info("✅ Encontrado:");
        Logs.info("   desc_marca              : " + encontrado.getDesc_marca());
        Logs.info("   desc_prefijo            : " + encontrado.getDesc_prefijo());
        Logs.info("   desc_prefijo_activacion : " + encontrado.getDesc_prefijo_activacion());

        Logs.info("✅ [TEST 3] PASADO");
    }
}