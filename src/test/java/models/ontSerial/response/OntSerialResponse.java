package models.network.response;

public class OntSerialItem {
    private String serial;
    private String descMarca;
    private String descPrefijo;
    private String descPrefijoActivacion;
    private String modelo;
    private String estado;
    private String mac;

    public OntSerialItem() {}

    public OntSerialItem(String serial, String descMarca, String descPrefijo,
                         String descPrefijoActivacion, String modelo,
                         String estado, String mac) {
        this.serial = serial;
        this.descMarca = descMarca;
        this.descPrefijo = descPrefijo;
        this.descPrefijoActivacion = descPrefijoActivacion;
        this.modelo = modelo;
        this.estado = estado;
        this.mac = mac;
    }

    public String getSerial() { return serial; }
    public void setSerial(String serial) { this.serial = serial; }

    public String getDescMarca() { return descMarca; }
    public void setDescMarca(String descMarca) { this.descMarca = descMarca; }

    public String getDescPrefijo() { return descPrefijo; }
    public void setDescPrefijo(String descPrefijo) { this.descPrefijo = descPrefijo; }

    public String getDescPrefijoActivacion() { return descPrefijoActivacion; }
    public void setDescPrefijoActivacion(String descPrefijoActivacion) {
        this.descPrefijoActivacion = descPrefijoActivacion;
    }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getMac() { return mac; }
    public void setMac(String mac) { this.mac = mac; }

    // Builder Pattern
    public static OntSerialItemBuilder builder() {
        return new OntSerialItemBuilder();
    }

    @Override
    public String toString() {
        return "OntSerialItem{" +
                "serial='" + serial + '\'' +
                ", descMarca='" + descMarca + '\'' +
                ", descPrefijo='" + descPrefijo + '\'' +
                ", descPrefijoActivacion='" + descPrefijoActivacion + '\'' +
                ", modelo='" + modelo + '\'' +
                ", estado='" + estado + '\'' +
                ", mac='" + mac + '\'' +
                '}';
    }

    public static class OntSerialItemBuilder {
        private String serial;
        private String descMarca;
        private String descPrefijo;
        private String descPrefijoActivacion;
        private String modelo;
        private String estado;
        private String mac;

        public OntSerialItemBuilder serial(String serial) {
            this.serial = serial;
            return this;
        }

        public OntSerialItemBuilder descMarca(String descMarca) {
            this.descMarca = descMarca;
            return this;
        }

        public OntSerialItemBuilder descPrefijo(String descPrefijo) {
            this.descPrefijo = descPrefijo;
            return this;
        }

        public OntSerialItemBuilder descPrefijoActivacion(String descPrefijoActivacion) {
            this.descPrefijoActivacion = descPrefijoActivacion;
            return this;
        }

        public OntSerialItemBuilder modelo(String modelo) {
            this.modelo = modelo;
            return this;
        }

        public OntSerialItemBuilder estado(String estado) {
            this.estado = estado;
            return this;
        }

        public OntSerialItemBuilder mac(String mac) {
            this.mac = mac;
            return this;
        }

        public OntSerialItem build() {
            return new OntSerialItem(serial, descMarca, descPrefijo,
                    descPrefijoActivacion, modelo, estado, mac);
        }
    }
}