package apis.fonowin;

import io.restassured.http.ContentType;
import models.fonowin.request.ReservationRequest;
import models.fonowin.response.ReservationResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;

import static io.restassured.RestAssured.given;

public class ReservationsTest extends BaseTest {

    private static final String RESERVATIONS_PATH =
            "/api/v1/fonowin/numbers/reservations";

    private ReservationRequest reservationRequest;

    @BeforeClass
    public void setupRequest() {
        reservationRequest = ReservationRequest.builder()
                .orderNumber(2888006)
                .requestedPhoneNumber("0")
                .userCode("Sistema")
                .sourceApplication("TELEGRAM")
                .portability(false)
                .build();

        Logs.info("Request preparado: " + reservationRequest);
    }

    @Test(groups = {regression})
    public void validarReservation() {
        Logs.info("POST " + RESERVATIONS_PATH);

        response = given()
                .spec(buildKongRequestSpec())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(reservationRequest)
                .when()
                .post(RESERVATIONS_PATH)
                .then()
                .extract()
                .response();

        Logs.info("Status code: " + response.getStatusCode());
        Logs.info("Body:\n" + response.asPrettyString());

        // ===== Validación HTTP =====
        Assert.assertEquals(response.getStatusCode(), 200, "Status HTTP inesperado");

        // ===== Deserialización =====
        ReservationResponse reservation = response.as(ReservationResponse.class);
        Assert.assertNotNull(reservation, "No se pudo deserializar la respuesta");
        Assert.assertNotNull(reservation.getMsg(), "msg es null");

        // ===== Validaciones del body =====
        ReservationResponse.Msg msg = reservation.getMsg();

        Assert.assertEquals(msg.getStatus(), 1, "Status lógico distinto de 1");
        Assert.assertEquals(msg.getResultado(), "Correcto", "Resultado inesperado");
        Assert.assertNotNull(msg.getNumero(), "numero es null");
        Assert.assertFalse(msg.getNumero().isEmpty(), "numero está vacío");

        Logs.info("Reservation validado: " + reservation);
    }
}