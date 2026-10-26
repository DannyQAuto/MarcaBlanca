package MarcaBlanca.CapadeIntegracion;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;
import utilities.TestDataCache;

public class CambioPlan extends BaseTest {

    @Test
    public void testCambioPlanVNO() {
        // 1. Configurar el entorno MBPlanta QA es la url
        switchToMBPlantaQAEnvironment();

       // 2. defino datos como string
        String codServicio = "1754431072599";
        String serieEquipo = "485754430E6105A9";
        int nuevoCodPlan = 2144;

        // 3. Construir el request body con codServicio como String
        String requestBody = String.format(
                "{\n" +
                        "    \"codServicio\": \"%s\",\n" +  // %s para String con comillas
                        "    \"serie\": \"%s\",\n" +
                        "    \"nuevoCodPlan\": %d\n" +
                        "}",
                codServicio,
                serieEquipo,
                nuevoCodPlan
        );

        Logs.info("Request para CambioPlanVNO:\n" + requestBody);

        // 4. Ejecutar la petición POST
        Response response = request
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post("/cambioplanvno");

        // 5. Validaciones y logs
        Logs.info("Respuesta de CambioPlanVNO:");
        response.prettyPrint();

        // Validar status code
        int statusCode = response.getStatusCode();
        Logs.info("Status Code: " + statusCode);
        Assert.assertEquals(statusCode, 200, "El código de estado debería ser 200");

        // Validar respuesta exitosa
        String mensajeRespuesta = response.jsonPath().getString("message");
        Logs.info("Mensaje de respuesta: " + mensajeRespuesta);

        // Validación opcional del mensaje
        if (mensajeRespuesta != null) {
            Assert.assertTrue(mensajeRespuesta.contains("éxito") ||
                            mensajeRespuesta.contains("correctamente"),
                    "El mensaje debería indicar éxito");
        }

        // 6. Guardar datos relevantes si es necesario
        if (statusCode == 200) {
            String nuevoPlan = response.jsonPath().getString("nuevoPlan");
            if (nuevoPlan != null) {
                TestDataCache.estadoReserva = "PLAN_CAMBIADO";
                Logs.info("Nuevo plan asignado: " + nuevoPlan);
            }
        }
    }
}