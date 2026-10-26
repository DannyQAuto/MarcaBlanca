package models.qualification.vertical.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QualificationItem {

    private Caja caja;
    private Integer puertos_dis;
    private Double por_puertos_dis;

    public QualificationItem() {}

    public Caja getCaja() { return caja; }
    public void setCaja(Caja caja) { this.caja = caja; }

    public Integer getPuertos_dis() { return puertos_dis; }
    public void setPuertos_dis(Integer puertos_dis) { this.puertos_dis = puertos_dis; }

    public Double getPor_puertos_dis() { return por_puertos_dis; }
    public void setPor_puertos_dis(Double por_puertos_dis) { this.por_puertos_dis = por_puertos_dis; }

    @Override
    public String toString() {
        return "QualificationItem{caja=" + caja +
                ", puertos_dis=" + puertos_dis +
                ", por_puertos_dis=" + por_puertos_dis + '}';
    }

    // =====================
    // Subclase Caja
    // =====================
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Caja {
        private String id_sitio;
        private String cod_sitio;
        private String tipo;
        private String estado;
        private String nombre_proyecto;
        private String cluster;
        private String id_ubigeo;

        public Caja() {}

        public String getId_sitio() { return id_sitio; }
        public void setId_sitio(String id_sitio) { this.id_sitio = id_sitio; }

        public String getCod_sitio() { return cod_sitio; }
        public void setCod_sitio(String cod_sitio) { this.cod_sitio = cod_sitio; }

        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }

        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }

        public String getNombre_proyecto() { return nombre_proyecto; }
        public void setNombre_proyecto(String nombre_proyecto) { this.nombre_proyecto = nombre_proyecto; }

        public String getCluster() { return cluster; }
        public void setCluster(String cluster) { this.cluster = cluster; }

        public String getId_ubigeo() { return id_ubigeo; }
        public void setId_ubigeo(String id_ubigeo) { this.id_ubigeo = id_ubigeo; }

        @Override
        public String toString() {
            return "Caja{id_sitio='" + id_sitio + "', cod_sitio='" + cod_sitio +
                    "', tipo='" + tipo + "', estado='" + estado +
                    "', proyecto='" + nombre_proyecto + "'}";
        }
    }
}