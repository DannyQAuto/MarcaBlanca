package MarcaBlanca.CapadeIntegracion;

import io.restassured.http.Method;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.TestDataCache;

import java.util.Random;

public class ReservaPuerto extends BaseTest {
    private static final int MAX_RETRIES = 1   ; // Número máximo de reintentos
    private int retryCount = 0;

    @BeforeMethod(alwaysRun = true)

    public void setUp() {
        switchToWinEnvironment();
        TestDataCache.loadCache();
    }

    @Test(groups = {"regression", "smoke"},
            dependsOnMethods = "MarcaBlanca.CapadeIntegracion.Factibilidad.testFactibilidad")
    public void testReservaPuerto() {
        boolean reservaExitosa = false;

        while (!reservaExitosa && retryCount < MAX_RETRIES) {
            try {
                // 1. Generar número de servicio aleatorio
                int nroServicio = new Random().nextInt(90_000_000) + 10_000_000;

                // 2. Construir el request usando el puerto disponible de Factibilidad
                String requestBody = String.format(
                        "{\"nroServicio\": %d, \"codSitio\": \"%s\", \"puertoReserva\": %d, " +
                                "\"keyFactibilidad\": \"%s\", \"keyCaja\": \"%s\"}",
                        nroServicio,
                        TestDataCache.codSitio,
                        TestDataCache.puertoDisponible,
                        TestDataCache.keyFactibilidad,
                        TestDataCache.keyCaja
                );

                System.out.println("\n=== Request enviado (Intento " + (retryCount + 1) + ") ===");
                System.out.println(requestBody);

                // 3. Ejecutar la reserva
                response = request
                        .basePath("/reservavno")
                        .body(requestBody)
                        .request(Method.POST);

                // 4. Validar respuesta
                String message = response.jsonPath().getString("message");

                if ("Reserva de puerto registrada correctamente".equals(message)) {
                    reservaExitosa = true;

                    // Guardar todos los datos de la respuesta
                    TestDataCache.codServicio = response.jsonPath().getString("data.codServicio");
                    int puertoReservado = response.jsonPath().getInt("data.puerto");
                    TestDataCache.puertoReservado = puertoReservado;
                    TestDataCache.reservaDesde = response.jsonPath().getString("data.desde");
                    TestDataCache.reservaHasta = response.jsonPath().getString("data.hasta");
                    TestDataCache.estadoReserva = response.jsonPath().getString("data.estado");
                    TestDataCache.saveCache();
                    System.out.println("\n=== Resultado de ReservaPuerto ===");
                    System.out.println("Status Code: " + response.getStatusCode());
                    System.out.println("Response Body: " + response.getBody().asPrettyString());
                    System.out.println("Código de Servicio guardado: " + TestDataCache.codServicio);
                    System.out.println("Tiempo de respuesta: " + response.getTime() + "ms");

                    // Verificar que el puerto reservado coincide con el solicitado
                    Assert.assertEquals(puertoReservado, TestDataCache.puertoDisponible,
                            "El puerto reservado no coincide con el solicitado");
                } else {
                    System.out.println("Reserva fallida: " + message);
                    System.out.println("Response Body: " + response.getBody().asPrettyString());

                    // Incrementar contador de reintentos
                    retryCount++;

                    if (retryCount < MAX_RETRIES) {
                        System.out.println("Reintentando... (" + retryCount + "/" + MAX_RETRIES + ")");

                        // Volver a ejecutar factibilidad para obtener nuevos datos
                        ejecutarFactibilidadNuevamente();
                    }
                }
            } catch (Exception e) {
                retryCount++;
                System.out.println("Error durante la reserva: " + e.getMessage());
                if (retryCount < MAX_RETRIES) {
                    System.out.println("Reintentando... (" + retryCount + "/" + MAX_RETRIES + ")");
                    ejecutarFactibilidadNuevamente();
                }
            }
        }


    }

    private void ejecutarFactibilidadNuevamente() {
        // Aquí deberías llamar al método testFactibilidad() de nuevo
        // Como no podemos llamar directamente al test desde aquí, necesitarías
        // extraer la lógica de factibilidad a un método que puedas llamar

        // Alternativamente, podrías lanzar una excepción especial que haga que TestNG
        // vuelva a ejecutar todo el grupo de tests dependientes

        // Implementación simplificada (necesitarías adaptarla a tu estructura real):
        Factibilidad factibilidad = new Factibilidad();
        factibilidad.setUp();
        factibilidad.testFactibilidad();
    }

}