package models.contextualization.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PhysicalInfoResponse {

    @JsonProperty("data")
    private Map<String, Object> data;   // genérico hasta ver JSON real

    @JsonProperty("message")
    private String message;

    @JsonProperty("status")
    private int status;

    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public boolean isExitoso() { return status == 0; }

    @Override
    public String toString() {
        return "PhysicalInfoResponse{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}