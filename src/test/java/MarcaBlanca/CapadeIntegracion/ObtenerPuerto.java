package MarcaBlanca.CapadeIntegracion;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.TestDataCache;

public class ObtenerPuerto extends BaseTest {

    @Test
    public void obtenerPuertoTest() {
        // 1. Cargar datos desde el cache


        // 2. Obtener codSitio directamente de la clase TestDataCache
        String codSitio = TestDataCache.codSitio;

        // 3. Validar que codSitio existe
        if (codSitio == null || codSitio.isEmpty()) {
            throw new RuntimeException("El código de sitio no está disponible en el cache. "
                    + "Asegúrate de haber ejecutado primero las pruebas que guardan este valor.");
        }

        // 4. Construir el request body
        String requestBody = String.format("{\"codSitio\": \"%s\"}", codSitio);
        System.out.println("Request Body: " + requestBody);

        // 5. Configurar entorno y enviar request
        switchToMBPlantaQAEnvironment();

        Response response = request
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(requestBody)
                .post("/obtenerpuertosvno");

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