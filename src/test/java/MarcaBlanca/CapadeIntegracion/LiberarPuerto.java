package MarcaBlanca.CapadeIntegracion;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.TestDataCache;

import static utilities.TestDataCache.puertoDisponible;

public class LiberarPuerto extends BaseTest {

    @Test
    public void LiberarPuertoTest() {
        // 1. Cargar datos desde el cache


        // 2. Obtener datos directamente de la clase TestDataCache
        String codSitio = TestDataCache.codSitio;
        //int puertoDisponible = TestDataCache.puertoDisponible;
        int puertoDisponible = 8;
        String keyFactibilidad = TestDataCache.keyFactibilidad;
        String keyCaja = TestDataCache.keyCaja;

        // 3. Validar que los datos existen
        if (codSitio == null || codSitio.isEmpty()) {
            throw new RuntimeException("El código de sitio no está disponible en el cache. "
                    + "Asegúrate de haber ejecutado primero las pruebas que guardan este valor.");
        }

        // 4. Construir el request body con todos los campos
        String requestBody = String.format(
                "{\"codSitio\": \"%s\", " +
                        "\"numPuerto\": %d, " +
                        "\"keyFactibilidad\": \"%s\", " +
                        "\"keyCaja\": \"%s\"}",
                codSitio,
                puertoDisponible,
                keyFactibilidad != null ? keyFactibilidad : "",
                keyCaja != null ? keyCaja : ""
        );

        System.out.println("Request Body: " + requestBody);

        // 5. Configurar entorno y enviar request
        switchToMBPlantaQAEnvironment();

        Response response = request
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(requestBody)
                .post("/liberarreservapuertovno");

        // 6. Validar respuesta
        softAssert.assertEquals(response.getStatusCode(), 200,
                "El servicio respondió con código: " + response.getStatusCode());

        softAssert.assertNotNull(response.getBody(),
                "El cuerpo de la respuesta no debe ser nulo");

        // 7. Imprimir respuesta para depuración
        System.out.println("Respuesta del servicio:");
        System.out.println("Status Code: " + response.getStatusCode());
        response.prettyPrint();

        // 8. Asegurar que todas las validaciones se ejecuten
        softAssert.assertAll();
    }
}