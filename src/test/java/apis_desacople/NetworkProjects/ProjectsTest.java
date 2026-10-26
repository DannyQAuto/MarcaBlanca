package apis.NetworkProjects;

import io.restassured.response.Response;
import models.projects.request.ProjectsRequest;
import models.projects.response.NetworkProject;
import models.projects.response.ProjectsResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;
import utilities.TokenManager;

import static io.restassured.RestAssured.given;

/**
 * Test del endpoint:
 *   GET /api/v1/site-projects?ubigeo=150131
 *
 * Patrón:
 *   - UNA SOLA llamada al endpoint en @BeforeClass
 *   - Los 2 tests reutilizan la respuesta cacheada
 *   - Sin duplicación de logs (el RequestFilter imprime request/response)
 */
public class ProjectsTest extends BaseTest {

    private static final String PROJECTS_PATH = "/api/v1/network-projects";
    private static final String UBIGEO = "150131";

    private ProjectsRequest projectsRequest;

    // Cache — se llena UNA SOLA VEZ en @BeforeClass
    private Response responseCache;
    private ProjectsResponse projectsCache;

    // ============================================================
    // SETUP: construir request + ÚNICA llamada al endpoint
    // ============================================================
    @BeforeClass
    public void setupProjectsTest() {
        Logs.info("🔧 [SETUP] ProjectsTest — única llamada al endpoint");

        projectsRequest = ProjectsRequest.builder()
                .ubigeo(UBIGEO)
                .build();

        String token = TokenManager.getKongAccessToken();

        responseCache = given()
                .spec(buildKongPublicSpec())
                .header("Authorization", "Bearer " + token)
                .queryParam("ubigeo", projectsRequest.getUbigeo())
                .basePath(PROJECTS_PATH)
                .when()
                .get();

        Assert.assertEquals(responseCache.getStatusCode(), 200,
                "El endpoint debe responder 200");

        projectsCache = responseCache.as(ProjectsResponse.class);
        Logs.info("💾 [SETUP] Respuesta cacheada. Los tests la reutilizarán.");
    }

    // ============================================================
    // TEST 1: wrapper + validar cada proyecto
    // ============================================================
    @Test(groups = {"smoke", "projects"})
    public void testObtenerNetworkProjectsConToken() {
        Logs.info("📍 TEST 1: OBTENER NETWORK PROJECTS — wrapper + cada proyecto");

        // Validaciones del wrapper
        Logs.info(String.format("   status=%d, message=%s, total=%d",
                projectsCache.getStatus(),
                projectsCache.getMessage(),
                projectsCache.getData() == null ? 0 : projectsCache.getData().size()));

        Assert.assertEquals(projectsCache.getStatus(), 0, "Status debe ser 0 (éxito)");
        Assert.assertEquals(projectsCache.getMessage(), "Success", "Message debe ser 'Success'");
        Assert.assertNotNull(projectsCache.getData(), "Data no debe ser null");
        Assert.assertFalse(projectsCache.getData().isEmpty(), "Data no debe estar vacío");

        // Validar cada proyecto
        Logs.info("📋 Validando cada proyecto:");
        for (NetworkProject proyecto : projectsCache.getData()) {
            Assert.assertNotNull(proyecto.getId(), "El id no debe ser null");
            Assert.assertNotNull(proyecto.getNomAgrupacion(), "nomAgrupacion no debe ser null");
            Assert.assertEquals(proyecto.getCodUbigeo(), UBIGEO,
                    "codUbigeo del proyecto debe coincidir con el enviado");

            Logs.info(String.format("   ✔ id=%s | %s | ubigeo=%s",
                    proyecto.getId(),
                    proyecto.getNomAgrupacion(),
                    proyecto.getCodUbigeo()));
        }

        Logs.info("✅ [TEST 1] PASADO");
    }

    // ============================================================
    // TEST 2: buscar proyecto específico (sobre la respuesta cacheada)
    // ============================================================
    @Test(groups = {"projects", "data-specific"},
            dependsOnMethods = "testObtenerNetworkProjectsConToken")
    public void testBuscarProyectoPorNombre() {
        Logs.info("📍 TEST 2: BUSCAR PROYECTO POR NOMBRE");

        String nombreBuscado = "BLAS CERDEÑA 480";
        Logs.info("🔎 Buscando proyecto: " + nombreBuscado);

        NetworkProject encontrado = null;
        for (NetworkProject proyecto : projectsCache.getData()) {
            if (nombreBuscado.equals(proyecto.getNomAgrupacion())) {
                encontrado = proyecto;
                break;
            }
        }

        Assert.assertNotNull(encontrado, "Debe existir el proyecto: " + nombreBuscado);
        Assert.assertEquals(encontrado.getId(), "34", "El id del proyecto debe ser 34");
        Assert.assertEquals(encontrado.getCodUbigeo(), UBIGEO, "El ubigeo debe coincidir");

        Logs.info("✅ Proyecto encontrado:");
        Logs.info("   id            : " + encontrado.getId());
        Logs.info("   nomAgrupacion : " + encontrado.getNomAgrupacion());
        Logs.info("   codUbigeo     : " + encontrado.getCodUbigeo());

        Logs.info("✅ [TEST 2] PASADO");
    }
}