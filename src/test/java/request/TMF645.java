package tests.qualification;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import request.CognitoRequests;
import request.QualificationRequestsTMF679;
import utilities.BaseTest;

import java.util.*;

@Epic("Qualification")
@Feature("Check Product Offering Qualification")
public class TMF679 extends BaseTest {

    private QualificationRequestsTMF679 qualificationRequests;
    private String accessToken;

    @BeforeClass
    public void setup() {
        System.out.println("\n🚀 INICIANDO PRUEBAS DE QUALIFICATION");

        CognitoRequests cognitoRequests = new CognitoRequests();
        accessToken = cognitoRequests.getTokenFromCacheOrNew();
        Assert.assertNotNull(accessToken, "El token no debe ser nulo");

        qualificationRequests = new QualificationRequestsTMF679(accessToken);
        System.out.println("✅ Token listo para qualification");
    }

    @Test(groups = {smoke, regression})
    @Severity(SeverityLevel.BLOCKER)
    @Description("Crear Qualification exitosamente con todos los headers")
    @Story("Crear qualification")
    public void testCreateQualificationSuccess() {
        System.out.println("\n📝 TEST: Crear Qualification exitosamente");

        String eventId = "E.20260702172045051.wintst";
        String messageId = "M.20260702172045051.wintst";

        // Headers requeridos
        Map<String, String> headers = new HashMap<>();
        headers.put("eventid", eventId);
        headers.put("messageid", messageId);
        headers.put("sarequestts", "2026-06-30T17:25:00.000Z");

        Map<String, Object> requestBody = buildQualificationBody(eventId, messageId);

        var response = qualificationRequests.createQualificationWithCustomHeaders(requestBody, headers);

        System.out.println("   Status: " + response.getStatusCode());
        System.out.println("   Response: " + response.getBody().asString());

        Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 201,
                "Status code debería ser 200 o 201. Actual: " + response.getStatusCode());

        System.out.println("✅ TEST COMPLETADO");
    }

    @Test(groups = {regression})
    @Severity(SeverityLevel.NORMAL)
    @Description("Crear Qualification con diferentes valores")
    @Story("Crear qualification con datos variables")
    public void testCreateQualificationWithDifferentData() {
        System.out.println("\n📝 TEST: Crear Qualification con diferentes datos");

        String eventId = "E.20260702172045052.wintst";
        String messageId = "M.20260702172045052.wintst"; // Debe coincidir con el header

        // Headers requeridos - DEBEN COINCIDIR con los del body
        Map<String, String> headers = new HashMap<>();
        headers.put("eventid", eventId);
        headers.put("messageid", messageId); // <-- AHORA COINCIDE
        headers.put("sarequestts", "2026-06-30T17:30:00.000Z");

        Map<String, Object> requestBody = buildQualificationBodyWithProduct("PROD-005", eventId, messageId);

        var response = qualificationRequests.createQualificationWithCustomHeaders(requestBody, headers);

        System.out.println("   Status: " + response.getStatusCode());
        System.out.println("   Response: " + response.getBody().asString());

        Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 201,
                "Status code debería ser 200 o 201. Actual: " + response.getStatusCode());

        System.out.println("✅ TEST COMPLETADO");
    }

    // Método para construir el body de qualification con valores específicos
    private Map<String, Object> buildQualificationBody(String eventId, String messageId) {
        return buildQualificationBodyWithProduct("PROD-004", eventId, messageId);
    }

    private Map<String, Object> buildQualificationBodyWithProduct(String productId, String eventId, String messageId) {
        Map<String, Object> body = new HashMap<>();
        body.put("@type", "CheckProductOfferingQualification");

        // Channel
        Map<String, Object> channel = new HashMap<>();
        channel.put("id", "1371");
        channel.put("name", "FCT-UNI");
        channel.put("@type", "ChannelRef");
        body.put("channel", channel);

        // CheckProductOfferingQualificationItem
        List<Map<String, Object>> items = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("id", "1");
        item.put("@type", "CheckProductOfferingQualificationItem");

        // Product
        Map<String, Object> product = new HashMap<>();
        product.put("@type", "Product");
        product.put("id", productId);

        // Place
        List<Map<String, Object>> places = new ArrayList<>();
        Map<String, Object> place = new HashMap<>();
        place.put("role", "installationAddress");
        place.put("@type", "RelatedPlaceRefOrValue");

        Map<String, Object> address = new HashMap<>();
        address.put("id", "15013200154332");
        address.put("country", "Peru");
        address.put("@type", "GeographicAddress");
        place.put("place", address);
        places.add(place);
        product.put("place", places);

        item.put("product", product);
        items.add(item);
        body.put("checkProductOfferingQualificationItem", items);

        // Characteristic - DEBE COINCIDIR con los headers
        List<Map<String, Object>> characteristics = new ArrayList<>();
        Map<String, Object> characteristic = new HashMap<>();
        characteristic.put("@type", "ObjectCharacteristic");
        characteristic.put("name", "checkProductOfferingQualification.event");

        Map<String, Object> value = new HashMap<>();
        value.put("eventId", eventId);    // <-- DEBE COINCIDIR con header eventid
        value.put("messageId", messageId); // <-- DEBE COINCIDIR con header messageid
        characteristic.put("value", value);
        characteristics.add(characteristic);
        body.put("characteristic", characteristics);

        return body;
    }
}