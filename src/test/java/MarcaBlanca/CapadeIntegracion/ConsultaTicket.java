import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.BaseTest;

public class ConsultaTicket extends BaseTest {

    @Test
    public void consultarTicketVNO() {
        // Configurar el entorno MBPlanta QA
        switchToMBPlantaQAEnvironment();

        // Cuerpo de la solicitud
        String requestBody = "{\n" +
                "    \"ticketConsulta\": \"TC-15\"\n" +
                "}";

        // Enviar la solicitud POST
        response = request
                .body(requestBody)
                .post("/consultarticketvno");

        // Validaciones básicas (puedes agregar más según necesites)
        softAssert.assertEquals(response.getStatusCode(), 200, "Verificar código de estado");
        softAssert.assertNotNull(response.getBody(), "Verificar que la respuesta tiene cuerpo");

        // También puedes imprimir la respuesta para depuración
        response.prettyPrint();
    }
}