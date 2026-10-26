package MarcaBlanca.Extras;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.BaseTest;

public class LiberarMacWoo extends BaseTest {

    @Test
    public void liberarMac() {
        // Configurar la URL base (si no está en BaseTest)
         request.baseUri("http://10.23.100.27");

        // Datos de la solicitud
        String numeroSerie = "48575443331B55B1"; // Tu único número de serie
        String ticked = "b99a20873abf2089562eea0dde99d1c2.78789008c5dfbc2b59ce3ab526666821.c7a3247008d30afd73d9cd0312083fe9";

        // Construir el cuerpo JSON
        String requestBody = String.format(
                "{" +
                        "  \"method\": \"logserviciosnce.unauth\"," +
                        "  \"params\": {" +
                        "    \"nserie\": \"%s\"," +
                        "    \"ticked\": \"%s\"" +
                        "  }" +
                        "}",
                numeroSerie, ticked
        );

        // Enviar la solicitud POST
        Response response = request
                .body(requestBody)
                .post("/frameservice/php/index.php");

        // Validaciones
        softAssert.assertEquals(response.getStatusCode(), 200, "Código de estado incorrecto");
        softAssert.assertNotNull(response.getBody(), "El cuerpo de la respuesta está vacío");

        // Imprimir respuesta (para debugging)
        System.out.println("Número de serie usado: " + numeroSerie);
        response.prettyPrint();
    }
}