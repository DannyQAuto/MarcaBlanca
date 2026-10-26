package apis.serviceactivations;

import io.restassured.http.ContentType;
import models.serviceactivations.request.ChecklistRequest;
import models.serviceactivations.response.ChecklistResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.BaseTest;
import utilities.Logs;

import static io.restassured.RestAssured.given;

public class ChecklistsTest extends BaseTest {

    private static final String CHECKLISTS_PATH =
            "/api/v1/service-activations/checklists";

    private ChecklistRequest checklistRequest;

    @BeforeClass
    public void setupRequest() {
        ChecklistRequest.TechnicalData technicalData =
                ChecklistRequest.TechnicalData.builder()
                        .orderNumber(121212)
                        .crmPlanCode("2818")
                        .ctoSiteId("00002379093")
                        .ctoPort("5")
                        .serialNumber("4857544399B733B7")
                        .ontProductId("2150088779QURA002223")
                        .ctoPower("")
                        .ontPower("-20.27")
                        .oltPower("-23.77")
                        .ontIp("10.211.21.129")
                        .ontStatus("ONT valida por servicio.")
                        .oltId("167772454")
                        .oltName("OLT2_200_MILLAS")
                        .routerModel("EG8145X6-10")
                        .ontLogicalPort("OLT2_200_MILLAS-0/17/4:19")
                        .vlan("10")
                        .routerBrand("Huawei")
                        .build();

        checklistRequest = ChecklistRequest.builder()
                .service("opticore")
                .action("activate")
                .technicalData(technicalData)
                .build();

        Logs.info("Request preparado: " + checklistRequest);
    }

    @Test(groups = {regression})
    public void validarChecklist() {
        Logs.info("PUT " + CHECKLISTS_PATH);

        response = given()
                .spec(buildKongRequestSpec())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(checklistRequest)
                .when()
                .put(CHECKLISTS_PATH)   // 👈 PUT, no POST
                .then()
                .extract()
                .response();

        Logs.info("Status code: " + response.getStatusCode());
        Logs.info("Body:\n" + response.asPrettyString());

        // ===== HTTP =====
        Assert.assertEquals(response.getStatusCode(), 200, "Status HTTP inesperado");

        // ===== Deserialización =====
        ChecklistResponse checklist = response.as(ChecklistResponse.class);
        Assert.assertNotNull(checklist, "No se pudo deserializar la respuesta");

        // ===== Validaciones flexibles hasta ver el JSON real =====
        if (checklist.getStatus() != null) {
            Assert.assertEquals(checklist.getStatus(), 0, "Status API distinto de 0");
            Assert.assertNotNull(checklist.getData(), "data es null");
            Assert.assertFalse(checklist.getData().isEmpty(), "data está vacío");

            ChecklistResponse.Data data = checklist.getData().get(0);
            Logs.info("Resultado bulk → status: " + data.getStatus()
                    + " | msg: " + data.getMsg());
        } else if (checklist.getMsg() != null) {
            ChecklistResponse.Msg msg = checklist.getMsg();
            Logs.info("Resultado fonowin → status: " + msg.getStatus()
                    + " | resultado: " + msg.getResultado());
        }

        Logs.info("Checklist validado: " + checklist);
    }
}