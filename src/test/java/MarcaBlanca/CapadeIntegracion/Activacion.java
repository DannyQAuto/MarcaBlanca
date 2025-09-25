package MarcaBlanca.CapadeIntegracion;

import io.restassured.http.Method;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.TestDataCache;

public class Activacion extends BaseTest {

    @Test(groups = {"regression", "smoke", "activaciones-group"},
            dependsOnMethods = {
                    "MarcaBlanca.CapadeIntegracion.Factibilidad.testFactibilidad",
                    "MarcaBlanca.CapadeIntegracion.ReservaPuerto.testReservaPuerto"
            })
    public void testActivacion() {
        // Verificar que tenemos el código de servicio
        if (TestDataCache.codServicio == null) {
            Assert.fail("No se encontró código de servicio en TestDataCache");
        }

        // Cambiar al entorno MBPlanta QA
        switchToMBPlantaQAEnvironment();

        // Construir request body
        String requestBody = String.format(
                "{\"codServicio\": %s, \"serie\": \"48575443331B55B1\", \"codPlan\":2152}",
                TestDataCache.codServicio
        );

        System.out.println("\n=== Request de Activación ===");
        System.out.println(requestBody);

        // Ejecutar activación
        response = request
                .basePath("/activacionvno")
                .body(requestBody)
                .request(Method.POST);

        // Validar respuesta
        Assert.assertEquals(response.getStatusCode(), 200);

        System.out.println("\n=== Resultados de Activación ===");
        System.out.println(response.getBody().asPrettyString());
        // Mostrar estado final del cache
        MarcaBlanca.CapadeIntegracion.TestDataCachePrinter.printFullCache();
    }
}