package utilities;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import listeners.SuiteListeners;
import listeners.TestListeners;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.asserts.SoftAssert;
import request.AuthRequests;

@Listeners({TestListeners.class, SuiteListeners.class})
public class BaseTest {
    // Constantes para configuración
    protected static final String DEFAULT_BASE_URI = "https://dummyjson.com/auth";
    protected static final String WIN_BASE_URI = "https://mbplanta-api.win.pe/mbplanta-ext/";
    protected static final String MBPLANTA_QA_URI = "https://mbplanta-api.win.pe/mbplanta-ext/";

    private static final String API_KEY = "Fv7oOMq9d931Pz6yLmL4cVy98aHJgP1h"; //woooo
    //private static final String API_KEY = "DW5YqNJRjsFfJN0uebDIuijRZg8SEUPw"; //bantel
    //private static final String API_KEY = "dsolOw8nmflS3kjooca5tpsdPeiX8us4"; //futuro net

    private static final String API_KEY_HEADER = "apikey";

    // Grupos de pruebas
    protected final String smoke = "smoke";
    protected final String regression = "regression";

    // Objetos compartidos
    protected SoftAssert softAssert;
    protected RequestSpecification request;
    protected Response response;

    @BeforeMethod(alwaysRun = true)
    public void masterSetUp() {
        // 1. Inicializar soft assertions
        softAssert = new SoftAssert();

        // 2. Cargar datos del cache antes de cada test
        TestDataCache.loadCache();
        Logs.debug("Cache cargado - Código Servicio: " + TestDataCache.codServicio);

        // 3. Configurar request base
        Logs.debug("Inicializando request base");
        request = RestAssured.given().spec(buildDefaultRequestSpec());
        new RequestProvider().set(request);
    }

    @AfterMethod(alwaysRun = true)
    public void masterTearDown() {
        // 1. Guardar estado actual en cache
        TestDataCache.saveCache();
        Logs.debug("Cache guardado - Código Servicio: " + TestDataCache.codServicio);

        // 2. Validar todas las aserciones
        softAssert.assertAll();
    }

    // Métodos de configuración de requests (sin cambios)
    private RequestSpecification buildDefaultRequestSpec() {
        return new RequestSpecBuilder()
                .addFilter(new RequestFilter())
                .setBaseUri(DEFAULT_BASE_URI)
                .setContentType(ContentType.JSON)
                .build();
    }

    protected RequestSpecification buildWinRequestSpec() {
        return new RequestSpecBuilder()
                .addFilter(new RequestFilter())
                .setBaseUri(WIN_BASE_URI)
                .setContentType(ContentType.JSON)
                .addHeader(API_KEY_HEADER, API_KEY)
                .build();
    }

    protected RequestSpecification buildMBPlantaQASpec() {
        return new RequestSpecBuilder()
                .addFilter(new RequestFilter())
                .setBaseUri(MBPLANTA_QA_URI)
                .setContentType(ContentType.JSON)
                .addHeader(API_KEY_HEADER, API_KEY)
                .build();
    }

    // Métodos para cambiar entorno (sin cambios)
    protected void switchToWinEnvironment() {
        Logs.debug("Cambiando a entorno WIN");
        request = RestAssured.given().spec(buildWinRequestSpec());
        new RequestProvider().set(request);
    }

    protected void switchToMBPlantaQAEnvironment() {
        Logs.debug("Cambiando a entorno MBPlanta QA");
        request = RestAssured.given().spec(buildMBPlantaQASpec());
        new RequestProvider().set(request);
    }

    @Step("Autenticación con el request de Login")
    protected void initAuth() {
        Logs.info("Autenticándose con el request de login");
        final var authRequest = new AuthRequests();
        authRequest.login("src/test/resources/requestsBody/login.json");
        final var token = ResponseManager.getPathAsString("accessToken");
        new RequestProvider().get().auth().preemptive().oauth2(token);
    }

    // Nuevo método helper para debug del cache
    protected void printCacheState(String message) {
        Logs.debug("\n=== Estado del Cache: " + message + " ===");
        Logs.debug("Código Sitio: " + TestDataCache.codSitio);
        Logs.debug("Código Servicio: " + TestDataCache.codServicio);
        Logs.debug("Puerto Reservado: " + TestDataCache.puertoReservado);
        Logs.debug("Reserva Desde-Hasta: " +
                TestDataCache.reservaDesde + " a " + TestDataCache.reservaHasta);
    }
}