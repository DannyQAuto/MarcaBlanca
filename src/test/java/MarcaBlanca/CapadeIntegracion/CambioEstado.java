import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.BaseTest;

public class CambioEstado extends BaseTest {

    @Test
    public void cambiarEstadoVNO() {
        // Configurar el entorno MBPlanta QA
        switchToMBPlantaQAEnvironment();

        // Cuerpo de la solicitud
        String requestBody = "{\n" +
                "    \"codServicio\": 1754431072599,\n" +
                "    \"serie\": \"485754430E6105A9\",\n" +
                "    \"estado\": \"false\"\n" +
                "}";

        // Enviar la solicitud POST
        response = request
                .body(requestBody)
                .post("/cambioestadovno");

        // Validaciones básicas
        softAssert.assertEquals(response.getStatusCode(), 200, "Verificar código de estado");
        softAssert.assertNotNull(response.getBody(), "Verificar que la respuesta tiene cuerpo");

        // Imprimir la respuesta para depuración
        response.prettyPrint();
    }
}