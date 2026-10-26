package models.bulk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReconnectionResponse {

    @JsonProperty("status")
    private int status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private List<Data> data;

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<Data> getData() { return data; }
    public void setData(List<Data> data) { this.data = data; }

    public boolean isExitoso() {
        return status == 0;
    }

    @Override
    public String toString() {
        return "ReconnectionResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    // ===== Clase interna Data =====
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

        @JsonProperty("status")
        private String status;

        @JsonProperty("msg")
        private String msg;

        @JsonProperty("nserie")
        private String nserie;

        @JsonProperty("npedido")
        private String npedido;

        @JsonProperty("nce_pedido")
        private String ncePedido;

        @JsonProperty("nce_estado")
        private String nceEstado;

        @JsonProperty("estado_new")
        private String estadoNew;

        @JsonProperty("encontrado")
        private String encontrado;

        @JsonProperty("encontrado_nserie")
        private String encontradoNserie;

        @JsonProperty("encontrado_pedido")
        private String encontradoPedido;

        @JsonProperty("fecha_ejecucion")
        private String fechaEjecucion;

        // Getters y Setters
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getMsg() { return msg; }
        public void setMsg(String msg) { this.msg = msg; }

        public String getNserie() { return nserie; }
        public void setNserie(String nserie) { this.nserie = nserie; }

        public String getNpedido() { return npedido; }
        public void setNpedido(String npedido) { this.npedido = npedido; }

        public String getNcePedido() { return ncePedido; }
        public void setNcePedido(String ncePedido) { this.ncePedido = ncePedido; }

        public String getNceEstado() { return nceEstado; }
        public void setNceEstado(String nceEstado) { this.nceEstado = nceEstado; }

        public String getEstadoNew() { return estadoNew; }
        public void setEstadoNew(String estadoNew) { this.estadoNew = estadoNew; }

        public String getEncontrado() { return encontrado; }
        public void setEncontrado(String encontrado) { this.encontrado = encontrado; }

        public String getEncontradoNserie() { return encontradoNserie; }
        public void setEncontradoNserie(String encontradoNserie) { this.encontradoNserie = encontradoNserie; }

        public String getEncontradoPedido() { return encontradoPedido; }
        public void setEncontradoPedido(String encontradoPedido) { this.encontradoPedido = encontradoPedido; }

        public String getFechaEjecucion() { return fechaEjecucion; }
        public void setFechaEjecucion(String fechaEjecucion) { this.fechaEjecucion = fechaEjecucion; }

        @Override
        public String toString() {
            return "Data{" +
                    "status='" + status + '\'' +
                    ", msg='" + msg + '\'' +
                    ", nserie='" + nserie + '\'' +
                    ", npedido='" + npedido + '\'' +
                    ", nceEstado='" + nceEstado + '\'' +
                    ", estadoNew='" + estadoNew + '\'' +
                    ", encontrado='" + encontrado + '\'' +
                    ", fechaEjecucion='" + fechaEjecucion + '\'' +
                    '}';
        }
    }
}