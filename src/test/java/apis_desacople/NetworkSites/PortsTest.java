package apis.networksites;

import io.restassured.http.ContentType;
import models.networksites.request.PortsRequest;
import models.networksites.response.PortsResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;

import java.util.List;

import static io.restassured.RestAssured.given;

public class PortsTest extends BaseTest {

    private static final String PORTS_PATH =
            "/api/v1/network-sites/ports";

    private static final List<Integer> BOX_IDS = List.of(2130002, 2478567, 1620081);
    private static final int PORT_STATUS = 2;

    private PortsRequest portsRequest;

    @BeforeClass
    public void setupRequest() {
        portsRequest = PortsRequest.builder()
                .boxIds(BOX_IDS)
                .portStatus(PORT_STATUS)
                .build();

        Logs.info("Request preparado: " + portsRequest);
    }

    @Test(groups = {regression})
    public void validarPorts() {
        Logs.info("POST " + PORTS_PATH);

        response = given()
                .spec(buildKongRequestSpec())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(portsRequest)
                .when()
                .post(PORTS_PATH)
                .then()
                .extract()
                .response();

        Logs.info("Status code: " + response.getStatusCode());
        Logs.info("Body:\n" + response.asPrettyString());

        // ===== HTTP =====
        Assert.assertEquals(response.getStatusCode(), 200, "Status HTTP inesperado");

        // ===== Deserialización =====
        PortsResponse ports = response.as(PortsResponse.class);
        Assert.assertNotNull(ports, "No se pudo deserializar la respuesta");

        // ===== Nivel raíz =====
        Assert.assertEquals(ports.getStatus(), "0", "Status lógico distinto de 0");
        Assert.assertEquals(ports.getMessage(), "Success", "message inesperado");
        Assert.assertNotNull(ports.getData(), "data es null");
        Assert.assertFalse(ports.getData().isEmpty(), "data está vacío");

        // ===== Recorrer cada caja =====
        for (PortsResponse.Caja caja : ports.getData()) {
            Logs.info("Validando caja id_sitio: " + caja.getIdSitio());

            Assert.assertTrue(caja.getIdSitio() > 0, "id_sitio inválido");
            Assert.assertNotNull(caja.getCodCajaAntiguo(), "cod_caja_antiguo es null");
            Assert.assertNotNull(caja.getCodCajaNuevo(), "cod_caja_nuevo es null");
            Assert.assertTrue(caja.getTotalPuertos() > 0, "total_puertos debe ser > 0");
            Assert.assertNotNull(caja.getPuertos(), "puertos es null");
            Assert.assertEquals(caja.getPuertos().size(), caja.getTotalPuertos(),
                    "Cantidad de puertos ≠ total_puertos en caja " + caja.getIdSitio());

            // Validar cada puerto
            for (PortsResponse.Puerto puerto : caja.getPuertos()) {
                Assert.assertNotNull(puerto.getPuerto(), "puerto es null");
                Assert.assertNotNull(puerto.getEstadoPuerto(), "estado_puerto es null");
                Assert.assertTrue(
                        puerto.getEstadoPuerto().equals("OCUPADO")
                                || puerto.getEstadoPuerto().equals("DISPONIBLE"),
                        "estado_puerto inesperado: " + puerto.getEstadoPuerto()
                );
            }
        }

        Logs.info("Ports validado correctamente. Total cajas: " + ports.getData().size());
    }
}