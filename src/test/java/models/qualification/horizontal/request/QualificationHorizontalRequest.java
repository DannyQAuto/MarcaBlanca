package models.qualification.horizontal.request;

public class QualificationRequest {

    private String boxType;
    private String condominiumId;
    private String floorNumber;
    private String towerId;
    private String latitude;
    private String longitude;

    private QualificationRequest(Builder builder) {
        this.boxType = builder.boxType;
        this.condominiumId = builder.condominiumId;
        this.floorNumber = builder.floorNumber;
        this.towerId = builder.towerId;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
    }

    public static Builder builder() { return new Builder(); }

    public String getBoxType() { return boxType; }
    public String getCondominiumId() { return condominiumId; }
    public String getFloorNumber() { return floorNumber; }
    public String getTowerId() { return towerId; }
    public String getLatitude() { return latitude; }
    public String getLongitude() { return longitude; }

    public static class Builder {
        private String boxType, condominiumId, floorNumber, towerId, latitude, longitude;

        public Builder boxType(String v) { this.boxType = v; return this; }
        public Builder condominiumId(String v) { this.condominiumId = v; return this; }
        public Builder floorNumber(String v) { this.floorNumber = v; return this; }
        public Builder towerId(String v) { this.towerId = v; return this; }
        public Builder latitude(String v) { this.latitude = v; return this; }
        public Builder longitude(String v) { this.longitude = v; return this; }

        public QualificationRequest build() { return new QualificationRequest(this); }
    }
}