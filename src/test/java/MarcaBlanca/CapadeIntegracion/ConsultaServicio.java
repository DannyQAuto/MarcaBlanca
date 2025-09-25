import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;

public class ConsultaServicio extends BaseTest {

    @Test
    public void consultarServicio() {
        // Ahora el código de servicio es un String
        consultarServicioVNO("1754412571121", "485754430E6105A9");
    }

    private void consultarServicioVNO(String codServicio, String serie) {
        // 1. Configurar entorno
        switchToMBPlantaQAEnvironment();

        // 2. Construir el request body con codServicio como string
        String requestBody = String.format("{\n" +
                "    \"codServicio\": \"%s\",\n" +  // Nota el %s y las comillas adicionales
                "    \"serie\": \"%s\"\n" +
                "}", codServicio, serie);

        // 3. Enviar la solicitud
        Response response = request
                .body(requestBody)
                .post("/consultaserviciovno");

        // 4. Validaciones
        softAssert.assertEquals(response.getStatusCode(), 200,
                "Código de estado debería ser 200");

        softAssert.assertNotNull(response.getBody(),
                "El cuerpo de la respuesta no debe ser nulo");

        // 5. Logging
        Logs.info("Consulta realizada con:\n" +
                "Código Servicio: " + codServicio + "\n" +
                "Serie: " + serie + "\n" +
                "Respuesta:");
        response.prettyPrint();
    }
}