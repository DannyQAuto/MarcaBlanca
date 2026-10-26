package models.qualification.horizontal.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QualificationResponse {

    private int status;
    private String message;
    private List<QualificationItem> data;

    public QualificationResponse() {}

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<QualificationItem> getData() { return data; }
    public void setData(List<QualificationItem> data) { this.data = data; }

    /** true si status == 0 */
    public boolean isExitoso() {
        return status == 0;
    }
}