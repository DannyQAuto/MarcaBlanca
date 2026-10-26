package models.network.request;

public class OntSerialRequest {
    private String serial;
    private String channel;

    public OntSerialRequest() {}

    public OntSerialRequest(String serial, String channel) {
        this.serial = serial;
        this.channel = channel;
    }

    public String getSerial() { return serial; }
    public void setSerial(String serial) { this.serial = serial; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    // Builder Pattern
    public static OntSerialRequestBuilder builder() {
        return new OntSerialRequestBuilder();
    }

    @Override
    public String toString() {
        return "OntSerialRequest{" +
                "serial='" + serial + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }

    public static class OntSerialRequestBuilder {
        private String serial;
        private String channel;

        public OntSerialRequestBuilder serial(String serial) {
            this.serial = serial;
            return this;
        }

        public OntSerialRequestBuilder channel(String channel) {
            this.channel = channel;
            return this;
        }

        public OntSerialRequest build() {
            return new OntSerialRequest(serial, channel);
        }
    }
}