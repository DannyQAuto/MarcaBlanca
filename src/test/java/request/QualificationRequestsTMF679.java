package request;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import utilities.BaseTest;
import utilities.ResponseManager;
import utilities.TestDataCache;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class QualificationRequestsTMF640 extends BaseTest {

    private static final String QUALIFICATION_ENDPOINT = "/tmf679/checkProductOfferingQualification";
    private String token;

    public QualificationRequestsTMF640() {
        if (TestDataCache.accessToken != null) {
            this.token = TestDataCache.accessToken;
        }
    }

    public QualificationRequestsTMF640(String token) {
        this.token = token;
    }

    @Step("Crear Qualification con headers personalizados")
    public Response createQualificationWithCustomHeaders(Map<String, Object> requestBody,
                                                         Map<String, String> customHeaders) {
        System.out.println("\n📋 CREANDO QUALIFICATION...");
        System.out.println("   Headers:");
        for (Map.Entry<String, String> entry : customHeaders.entrySet()) {
            System.out.println("     " + entry.getKey() + ": " + entry.getValue());
        }

        validateToken();

        // Agregar headers por defecto
        Map<String, String> allHeaders = new java.util.HashMap<>();
        allHeaders.put("eventid", "E.20260702172045051.wintst");
        allHeaders.put("messageid", "M.20260702172045051.wintst");
        allHeaders.put("sarequestts", "2026-06-30T17:25:00.000Z");
        if (customHeaders != null) {
            allHeaders.putAll(customHeaders);
        }

        Response response = given()
                .spec(buildQualificationRequestSpec(this.token))
                .headers(allHeaders)
                .body(requestBody)
                .when()
                .post(QUALIFICATION_ENDPOINT)
                .then()
                .extract()
                .response();

        ResponseManager.setResponse(response);

        System.out.println("   Status: " + response.getStatusCode());
        if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
            System.out.println("✅ QUALIFICATION CREADA");
            String id = response.jsonPath().getString("id");
            if (id != null && !id.isEmpty()) {
                System.out.println("   ID: " + id);
            }
        } else {
            System.out.println("❌ ERROR");
            System.out.println("   Mensaje: " + response.getBody().asString());
        }
        System.out.println("");

        return response;
    }

    private void validateToken() {
        if (this.token == null || this.token.isEmpty()) {
            System.out.println("⚠️ Token no disponible, obteniendo nuevo...");
            CognitoRequests cognitoRequests = new CognitoRequests();
            this.token = cognitoRequests.getTokenAndStore();
        }
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}