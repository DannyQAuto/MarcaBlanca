package apis.qualification;

import io.restassured.response.Response;
import models.qualification.horizontal.request.QualificationHorizontalRequest;
import models.qualification.horizontal.response.QualificationHorizontalItem;
import models.qualification.horizontal.response.QualificationHorizontalResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;
import utilities.TokenManager;

import static io.restassured.RestAssured.given;

/**
 * ============================================================
 *  TEST: Service Qualification / Factibility (HORIZONTAL)
 * ============================================================
 *  GET /api/v1/service-qualification/factibility
 *  boxType=7, lat/lng de San Isidro
 *
 *  ✔ UNA SOLA llamada al endpoint en @BeforeClass.
 *  ✔ Los 3 tests comparten la respuesta cacheada.
 * ============================================================
 */
public class QualificationHorizontalTest extends BaseTest {

    private static final String PATH = "/api/v1/service-qualification/factibility";
    private static final String COD_BUSCADO = "WN-20A-2026133";

    private QualificationHorizontalRequest request;
    private Response responseCache;
    private QualificationHorizontalResponse resCache;

    // ============================================================
    // SETUP — ÚNICA LLAMADA AL ENDPOINT
    // ============================================================
    @BeforeClass
    public void setupAndCallOnce() {
        Logs.info("🔧 [SETUP] QualificationHorizontalTest — única llamada al endpoint");

        request = QualificationHorizontalRequest.builder()
                .boxType("7")
                .condominiumId("")
                .floorNumber("")
                .towerId("")
                .latitude("-12.0976773")
                .longitude("-77.0231155")
                .build();

        String token = TokenManager.getKongAccessToken();

        responseCache = given()
                .spec(buildKongPublicSpec())
                .header("Authorization", "Bearer " + token)
                .queryParam("boxType", request.getBoxType())
                .queryParam("condominiumId", request.getCondominiumId())
                .queryParam("floorNumber", request.getFloorNumber())
                .queryParam("towerId", request.getTowerId())
                .queryParam("latitude", request.getLatitude())
                .queryParam("longitude", request.getLongitude())
                .basePath(PATH)
                .when()
                .get();

        Assert.assertEquals(responseCache.getStatusCode(), 200,
                "El endpoint debe responder 200");

        resCache = responseCache.as(QualificationHorizontalResponse.class);
        Logs.info("💾 [SETUP] Respuesta cacheada. Los 3 tests la reutilizarán.");
    }

    // ============================================================
    // TEST 1 — Wrapper
    // ============================================================
    @Test(groups = {"smoke", "qualification", "horizontal"})
    public void testObtenerQualificationHorizontal() {
        Logs.info("📍 TEST 1: HORIZONTAL — Wrapper");

        Logs.info(String.format("   status=%d, message=%s, data.size=%d",
                resCache.getStatus(),
                resCache.getMessage(),
                resCache.getData() == null ? 0 : resCache.getData().size()));

        Assert.assertTrue(resCache.isExitoso(), "El flag isExitoso() debe ser true");
        Assert.assertEquals(resCache.getMessage(), "Success");
        Assert.assertNotNull(resCache.getData(), "data no debe ser null");
        Assert.assertFalse(resCache.getData().isEmpty(), "data no debe estar vacío");

        Logs.info("✅ [TEST 1] PASADO");
    }

    // ============================================================
    // TEST 2 — Validar cada caja
    // ============================================================
    @Test(groups = {"qualification", "horizontal"},
            dependsOnMethods = "testObtenerQualificationHorizontal")
    public void testValidarCadaCajaHorizontal() {
        Logs.info("📍 TEST 2: HORIZONTAL — Validar cada CAJA (CTO)");
        Logs.info("📋 Recorriendo " + resCache.getData().size() + " cajas");

        int index = 0;
        for (QualificationHorizontalItem item : resCache.getData()) {
            String ctx = "[item " + index + "]";

            Assert.assertNotNull(item.getCaja(), ctx + " getCaja() no debe ser null");
            Assert.assertNotNull(item.getCaja().getId_sitio(), ctx + " id_sitio no debe ser null");
            Assert.assertNotNull(item.getCaja().getCod_sitio(), ctx + " cod_sitio no debe ser null");
            Assert.assertEquals(item.getCaja().getTipo(), "CTO", ctx + " tipo debe ser CTO");
            Assert.assertNotNull(item.getPuertos_dis(), ctx + " puertos_dis no debe ser null");
            Assert.assertTrue(item.getPor_puertos_dis() >= 0, ctx + " por_puertos_dis debe ser >= 0");

            Logs.info(String.format("   ✔ %s | %s | %s | puertos_dis=%s (%s%%)",
                    item.getCaja().getCod_sitio(),
                    item.getCaja().getEstado(),
                    item.getCaja().getNombre_proyecto(),
                    item.getPuertos_dis(),
                    item.getPor_puertos_dis()));

            index++;
        }

        Logs.info("✅ [TEST 2] PASADO");
    }

    // ============================================================
    // TEST 3 — Buscar por cod_sitio
    // ============================================================
    @Test(groups = {"qualification", "horizontal", "data-specific"},
            dependsOnMethods = "testValidarCadaCajaHorizontal")
    public void testBuscarCajaPorCodSitioHorizontal() {
        Logs.info("📍 TEST 3: HORIZONTAL — Buscar caja por cod_sitio");
        Logs.info("🔎 Buscando: " + COD_BUSCADO);

        QualificationHorizontalItem encontrado = null;
        for (QualificationHorizontalItem item : resCache.getData()) {
            if (item.getCaja() != null && COD_BUSCADO.equals(item.getCaja().getCod_sitio())) {
                encontrado = item;
                break;
            }
        }

        Assert.assertNotNull(encontrado, "Debe existir la caja: " + COD_BUSCADO);
        Assert.assertEquals(encontrado.getCaja().getId_sitio(), "00002026133");
        Assert.assertEquals(encontrado.getCaja().getTipo(), "CTO");

        Logs.info("✅ Encontrado:");
        Logs.info("   " + encontrado.getCaja().getCod_sitio()
                + " | " + encontrado.getCaja().getEstado()
                + " | " + encontrado.getCaja().getNombre_proyecto()
                + " | cluster=" + encontrado.getCaja().getCluster()
                + " | ubigeo=" + encontrado.getCaja().getId_ubigeo());

        Logs.info("✅ [TEST 3] PASADO");
    }
}