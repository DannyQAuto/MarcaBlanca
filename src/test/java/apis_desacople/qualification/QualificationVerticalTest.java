package apis.qualification;

import io.restassured.response.Response;
import models.qualification.vertical.request.QualificationVerticalRequest;
import models.qualification.vertical.response.QualificationVerticalItem;
import models.qualification.vertical.response.QualificationVerticalResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;
import utilities.TokenManager;

import static io.restassured.RestAssured.given;

/**
 * ============================================================
 *  TEST: Service Qualification / Factibility (VERTICAL)
 * ============================================================
 *  GET /api/v1/service-qualification/factibility
 *  boxType=8, condominio 2066, torre 8781
 *
 *  ✔ UNA SOLA llamada al endpoint en @BeforeClass.
 *  ✔ Los 3 tests comparten la respuesta cacheada.
 * ============================================================
 */
public class QualificationVerticalTest extends BaseTest {

    private static final String PATH = "/api/v1/service-qualification/factibility";
    private static final String COD_BUSCADO = "WN-230-1111146";

    private QualificationVerticalRequest request;
    private Response responseCache;
    private QualificationVerticalResponse resCache;

    // ============================================================
    // SETUP — ÚNICA LLAMADA AL ENDPOINT
    // ============================================================
    @BeforeClass
    public void setupAndCallOnce() {
        Logs.info("🔧 [SETUP] QualificationVerticalTest — única llamada al endpoint");

        request = QualificationVerticalRequest.builder()
                .boxType("8")
                .condominiumId("2066")
                .floorNumber("2")
                .towerId("8781")
                .latitude("-12.108237653")
                .longitude("-77.04892839")
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

        resCache = responseCache.as(QualificationVerticalResponse.class);
        Logs.info("💾 [SETUP] Respuesta cacheada. Los 3 tests la reutilizarán.");
    }

    // ============================================================
    // TEST 1 — Wrapper
    // ============================================================
    @Test(groups = {"smoke", "qualification", "vertical"})
    public void testObtenerQualificationVertical() {
        Logs.info("📍 TEST 1: VERTICAL — Wrapper");

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
    @Test(groups = {"qualification", "vertical"},
            dependsOnMethods = "testObtenerQualificationVertical")
    public void testValidarCadaCajaVertical() {
        Logs.info("📍 TEST 2: VERTICAL — Validar cada CAJA (NAP)");
        Logs.info("📋 Recorriendo " + resCache.getData().size() + " cajas");

        int index = 0;
        for (QualificationVerticalItem item : resCache.getData()) {
            String ctx = "[item " + index + "]";

            Assert.assertNotNull(item.getCaja(), ctx + " getCaja() no debe ser null");
            Assert.assertNotNull(item.getCaja().getId_sitio(), ctx + " id_sitio no debe ser null");
            Assert.assertNotNull(item.getCaja().getCod_sitio(), ctx + " cod_sitio no debe ser null");
            Assert.assertEquals(item.getCaja().getTipo(), "NAP", ctx + " tipo debe ser NAP");
            Assert.assertNotNull(item.getPuertos_dis(), ctx + " puertos_dis no debe ser null");
            Assert.assertTrue(item.getPor_puertos_dis() >= 0, ctx + " por_puertos_dis debe ser >= 0");

            Logs.info(String.format("   ✔ %s | %s | %s | sub=%s | torre=%s | puertos_dis=%s (%s%%)",
                    item.getCaja().getCod_sitio(),
                    item.getCaja().getEstado(),
                    item.getCaja().getNombre_proyecto(),
                    item.getCaja().getNombre_subproyecto(),
                    item.getCaja().getTorre(),
                    item.getPuertos_dis(),
                    item.getPor_puertos_dis()));

            index++;
        }

        Logs.info("✅ [TEST 2] PASADO");
    }

    // ============================================================
    // TEST 3 — Buscar por cod_sitio
    // ============================================================
    @Test(groups = {"qualification", "vertical", "data-specific"},
            dependsOnMethods = "testValidarCadaCajaVertical")
    public void testBuscarCajaPorCodSitioVertical() {
        Logs.info("📍 TEST 3: VERTICAL — Buscar caja por cod_sitio");
        Logs.info("🔎 Buscando: " + COD_BUSCADO);

        QualificationVerticalItem encontrado = null;
        for (QualificationVerticalItem item : resCache.getData()) {
            if (item.getCaja() != null && COD_BUSCADO.equals(item.getCaja().getCod_sitio())) {
                encontrado = item;
                break;
            }
        }

        Assert.assertNotNull(encontrado, "Debe existir la caja: " + COD_BUSCADO);
        Assert.assertEquals(encontrado.getCaja().getId_sitio(), "00001111146");
        Assert.assertEquals(encontrado.getCaja().getTipo(), "NAP");

        Logs.info("✅ Encontrado:");
        Logs.info("   " + encontrado.getCaja().getCod_sitio()
                + " | " + encontrado.getCaja().getEstado()
                + " | " + encontrado.getCaja().getNombre_proyecto()
                + " | sub=" + encontrado.getCaja().getNombre_subproyecto()
                + " | torre=" + encontrado.getCaja().getTorre()
                + " | cluster=" + encontrado.getCaja().getCluster()
                + " | ubigeo=" + encontrado.getCaja().getId_ubigeo());

        Logs.info("✅ [TEST 3] PASADO");
    }
}