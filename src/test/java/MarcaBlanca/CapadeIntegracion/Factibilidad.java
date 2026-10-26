package MarcaBlanca.CapadeIntegracion;

import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.TestDataCache;

import java.util.Random;

public class Factibilidad extends BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        switchToWinEnvironment();
    }

    @Test(groups = {"regression", "smoke", "factibilidad-group"}) // Grupo añadido
    public void testFactibilidad() {
        Random random = new Random();

        // Generar 3-4 dígitos random para la parte decimal
        int randomDecimalLat = random.nextInt(10000); // 0-9999
        int randomDecimalLon = random.nextInt(10000); // 0-9999

        // Mantener la parte entera fija y variar solo los decimales
        String latitud = String.format("-12.0603%d", randomDecimalLat);
        String longitud = String.format("-77.0483%d", randomDecimalLon);


        /*String latitud = String.format("-12.045410256");
        String longitud = String.format("-77.01357179");

*/
        String requestBody = String.format(
                "{\"latitud\": \"%s\", \"longitud\": \"%s\", \"tipoCaja\": 7}",
                latitud, longitud
        );

        System.out.println("\n=== Request de Factibilidad ===");
        System.out.println(requestBody);

        response = request
                .basePath("/factibilidadvno")
                .body(requestBody)
                .request(Method.POST);

        Assert.assertEquals(response.getStatusCode(), 200);

        JsonPath jsonPath = response.jsonPath();
        TestDataCache.codSitio = jsonPath.getString("data[0].caja.codSitio");
        TestDataCache.keyFactibilidad = jsonPath.getString("keyFactibilidad");
        TestDataCache.keyCaja = jsonPath.getString("data[0].keyCaja");
        TestDataCache.puertoDisponible = jsonPath.getInt("data[0].puerto[0].puerto");

        System.out.println("\n=== Resultados de Factibilidad ===");
        System.out.println(response.getBody().asPrettyString());
    }
    @AfterSuite
    public void tearDownSuite() {
        TestDataCache.saveCache(); // Guarda los datos actuales
    }
}