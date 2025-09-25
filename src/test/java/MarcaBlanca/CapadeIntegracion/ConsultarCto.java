package MarcaBlanca.CapadeIntegracion;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;

public class ConsultarCto extends BaseTest {

    @Test
    public void consultarCTOPuerto() {
        // 1. Configurar el entorno MBPlanta QA
        switchToMBPlantaQAEnvironment();

        // 2. Definir el código de servicio como String
        String codigoServicio = "1754431072599";

        // 3. Construir el cuerpo de la solicitud con codigoServicio como String
        String requestBody = String.format("{\n" +
                "    \"codigoServicio\": \"%s\"\n" +  //
                "}", codigoServicio);

        Logs.info("Request para consulta CTO Puerto:\n" + requestBody);

        // 4. Enviar la solicitud POST
        Response response = request
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post("/consultactopuerto");

        // 5. Validaciones y logs
        Logs.info("Respuesta del servicio:");
        response.prettyPrint();

        // Validar status code
        int statusCode = response.getStatusCode();
        Logs.info("Status Code: " + statusCode);
        Assert.assertEquals(statusCode, 200, "El código de estado debería ser 200");

        // Validar que la respuesta contiene datos esperados
        String ctoId = response.jsonPath().getString("ctoId");



    }


}