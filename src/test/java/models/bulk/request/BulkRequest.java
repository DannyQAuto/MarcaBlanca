package models.bulk.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReconnectionRequest {

    @JsonProperty("processType")
    private String processType;

    @JsonProperty("orders")
    private List<Order> orders;

    // ===== Clase interna Order (cada item del array) =====
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Order {

        @JsonProperty("orderCode")
        private String orderCode;

        @JsonProperty("serialNumber")
        private String serialNumber;
    }
}