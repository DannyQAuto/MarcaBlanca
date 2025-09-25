package MarcaBlanca.CapadeIntegracion;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.TestDataCache;

public class ModificarPuerto extends BaseTest {

    @Test
    public void modificarPuertoTest() {
        // 1. Obtener datos del cache
        String codServicio = TestDataCache.codServicio;
        String codSitio = TestDataCache.codSitio;
        int nuevoPuerto = 13; // Puerto que queremos asignar

        // 2. Validaciones de datos
        if (codServicio == null || codServicio.isEmpty()) {
            throw new RuntimeException("El código de servicio no está en el cache.");
        }
        if (codSitio == null || codSitio.isEmpty()) {
            throw new RuntimeException("El código de sitio no está en el cache.");
        }

        // 3. Construir request body
        String requestBody = String.format(
                "{\"codServicio\": \"%s\", \"codSitio\": \"%s\", \"puerto\": %d}",
                codServicio, codSitio, nuevoPuerto
        );

        System.out.println("Request Body: " + requestBody);

        // 4. Configurar y enviar request
        switchToMBPlantaQAEnvironment();

        Response response = request
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(requestBody)
                .post("/modreservapuertovno");

        // 5. Validar respuesta
        softAssert.assertEquals(response.getStatusCode(), 200,
                "Código de estado inesperado: " + response.getStatusCode());

        softAssert.assertNotNull(response.getBody(), "Respuesta vacía");

        // 6. Si la respuesta es exitosa, actualizar el puerto en TestDataCache
        if (response.getStatusCode() == 200) {
            TestDataCache.puertoDisponible = nuevoPuerto; // Actualiza el puerto en el cache
            System.out.println("✅ Puerto actualizado en TestDataCache: " + nuevoPuerto);
        }

        // 7. Mostrar respuesta
        System.out.println("Respuesta (" + response.getStatusCode() + "):");
        response.prettyPrint();

        // 8. Finalizar validaciones
        softAssert.assertAll();
    }
}