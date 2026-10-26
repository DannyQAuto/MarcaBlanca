package apis.NetworkProjects;

import io.restassured.response.Response;
import models.subprojects.request.SubProjectsRequest;
import models.subprojects.response.SubProject;
import models.subprojects.response.SubProjectsResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;
import utilities.TokenManager;

import static io.restassured.RestAssured.given;

/**
 * Test del endpoint:
 *   GET /api/v1/site-projects/{projectId}/sub-projects
 *
 * Ejemplo: GET /api/v1/site-projects/2066/sub-projects
 *
 * ✔ UNA SOLA llamada al endpoint en @BeforeClass.
 * ✔ Los 2 tests comparten la respuesta cacheada.
 */
public class SubProjectsTest extends BaseTest {

    private static final String SUB_PROJECTS_PATH = "/api/v1/network-projects/%s/sub-projects";
    private static final String PROJECT_ID = "2066";

    private SubProjectsRequest subProjectsRequest;
    private Response responseCache;
    private SubProjectsResponse resCache;

    // ============================================================
    // SETUP: construir request + ÚNICA llamada al endpoint
    // ============================================================
    @BeforeClass
    public void setupAndCallOnce() {
        Logs.info("🔧 [SETUP] SubProjectsTest — projectId=" + PROJECT_ID);

        subProjectsRequest = SubProjectsRequest.builder()
                .projectId(PROJECT_ID)
                .build();

        String token = TokenManager.getKongAccessToken();
        String finalPath = String.format(SUB_PROJECTS_PATH,
                subProjectsRequest.getProjectId());

        responseCache = given()
                .spec(buildKongPublicSpec())
                .header("Authorization", "Bearer " + token)
                .basePath(finalPath)
                .when()
                .get();

        Assert.assertEquals(responseCache.getStatusCode(), 200,
                "El endpoint debe responder 200");

        resCache = responseCache.as(SubProjectsResponse.class);
        Logs.info("💾 [SETUP] Respuesta cacheada. Los 2 tests la reutilizarán.");
    }

    // ============================================================
    // TEST 1: wrapper + validar cada sub-proyecto
    // ============================================================
    @Test(groups = {"smoke", "subprojects"})
    public void testObtenerSubProjectsConToken() {
        Logs.info("📍 TEST 1: OBTENER SUB-PROJECTS — wrapper + cada sub-proyecto");

        Logs.info(String.format("   status=%d, message=%s, total=%d",
                resCache.getStatus(),
                resCache.getMessage(),
                resCache.getData() == null ? 0 : resCache.getData().size()));

        // Wrapper (OJO: el mensaje viene en minúscula "success")
        Assert.assertTrue(resCache.isExitoso(),
                "isExitoso() debe ser true (status == 0)");
        Assert.assertEquals(resCache.getMessage(), "success",
                "Message debe ser 'success' (minúscula)");
        Assert.assertNotNull(resCache.getData(), "Data no debe ser null");
        Assert.assertFalse(resCache.getData().isEmpty(), "Data no debe estar vacío");

        // Validar cada sub-proyecto
        Logs.info("📋 Validando cada sub-proyecto:");
        int index = 0;
        for (SubProject subProyecto : resCache.getData()) {
            String ctx = "[item " + index + "]";

            Assert.assertNotNull(subProyecto.getId(), ctx + " id no debe ser null");
            Assert.assertNotNull(subProyecto.getNombre(), ctx + " nombre no debe ser null");

            Logs.info(String.format("   ✔ id=%s | nombre=%s",
                    subProyecto.getId(),
                    subProyecto.getNombre()));

            index++;
        }

        Logs.info("✅ [TEST 1] PASADO");
    }

    // ============================================================
    // TEST 2: buscar sub-proyecto específico (sobre cache)
    // ============================================================
    @Test(groups = {"subprojects", "data-specific"},
            dependsOnMethods = "testObtenerSubProjectsConToken")
    public void testBuscarSubProyectoPorNombre() {
        Logs.info("📍 TEST 2: BUSCAR SUB-PROYECTO POR NOMBRE");

        String nombreBuscado = "A";
        Logs.info("🔎 Buscando sub-proyecto: " + nombreBuscado);

        SubProject encontrado = null;
        for (SubProject subProyecto : resCache.getData()) {
            if (nombreBuscado.equals(subProyecto.getNombre())) {
                encontrado = subProyecto;
                break;
            }
        }

        Assert.assertNotNull(encontrado,
                "Debe existir el sub-proyecto: " + nombreBuscado);
        Assert.assertEquals(encontrado.getId(), "8764",
                "El id debe ser 8764");

        Logs.info("✅ Sub-proyecto encontrado:");
        Logs.info("   id     : " + encontrado.getId());
        Logs.info("   nombre : " + encontrado.getNombre());

        Logs.info("✅ [TEST 2] PASADO");
    }
}