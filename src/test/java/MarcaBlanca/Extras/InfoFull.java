package MarcaBlanca.Extras;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.BaseTest;

import java.util.Arrays;

public class InfoFull extends BaseTest {

    @Test
    public void InfoFull1() {

        request.baseUri("http://10.23.100.27");

        // Datos de la solicitud
        String numeroSerie = "48575443331B55B1"; //
        String ticked = "b99a20873abf2089562eea0dde99d1c2.78789008c5dfbc2b59ce3ab526666821.c7a3247008d30afd73d9cd0312083fe9";

        // Construir el cuerpo JSON
        String requestBody = String.format(
                "{" +
                        "  \"method\": \"logserviciosnce.getinfofull\"," +
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

        // Validaciones básicas
        softAssert.assertEquals(response.getStatusCode(), 200, "Código de estado incorrecto");
        softAssert.assertNotNull(response.getBody(), "El cuerpo de la respuesta está vacío");

        // Validación del campo "vas"
        String vasValue = response.jsonPath().getString("data.vas");
        String vlanidTest = response.jsonPath().getString("data.services_ports.vlanid");

        softAssert.assertNotNull(vasValue, "El campo 'vas' no existe o es nulo");

        // Lista de valores permitidos
        String[] valoresPermitidos = {"VAS_INTERNET_FUTURO", "VAS_INTERNET_JSP", "VAS_INTERNET_BANTEL"};

        // Validar que el valor esté en la lista permitida
        softAssert.assertTrue(
                Arrays.asList(valoresPermitidos).contains(vasValue),
                String.format("El valor de 'vas' (%s) no es uno de los permitidos: %s",
                        vasValue, Arrays.toString(valoresPermitidos))
        );

        // Imprimir respuesta (para debugging)
        System.out.println("Número de serie usado: " + numeroSerie);
        System.out.println("Valor de vas: " + vasValue);
        System.out.println("multivlans: " + vlanidTest);
        response.prettyPrint();

        // Ejecutar todas las validaciones
        softAssert.assertAll();

    }}