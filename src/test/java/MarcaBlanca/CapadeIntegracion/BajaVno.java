import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.BaseTest;

public class BajaVno extends BaseTest {

    @Test
    public void ejecutarBaja() {
        // 1. Configurar el entorno MBPlanta QA
        switchToMBPlantaQAEnvironment();

        // 2. Cuerpo de la solicitud CORREGIDO (eliminada coma sobrante)
        String requestBody = """
            {
                "codServicio": 1755536544260,
                "serie": "485754430E6105A9"
            }""";

        // 3. Enviar la solicitud POST con headers adecuados
        Response response = request
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(requestBody)
                .post("/bajavno");

        // 4. Validaciones mejoradas
        softAssert.assertEquals(response.getStatusCode(), 200,
                "El servicio respondió con error: " + response.getBody().asString());

        softAssert.assertNotNull(response.getBody(),
                "El cuerpo de la respuesta no debe ser nulo");

        // 5. Imprimir respuesta detallada
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body:");
        response.prettyPrint();
    }
}