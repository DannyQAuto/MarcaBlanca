package utilities;

import java.io.*;
import java.util.Properties;

public class TestDataCache {
    // Variables estáticas públicas
    public static String codSitio;
    public static String keyFactibilidad;
    public static String keyCaja;
    public static int puertoDisponible;
    public static String codServicio;
    public static int puertoReservado;
    public static String reservaDesde;
    public static String reservaHasta;
    public static String estadoReserva;

    // Archivo de cache en directorio temporal del sistema
    private static final String CACHE_FILE = System.getProperty("java.io.tmpdir") + "/testdata_cache.properties";

    /**
     * Guarda el estado actual del cache
     */
    public static synchronized void saveCache() {
        Properties props = new Properties();

        // Guardar todas las variables
        props.setProperty("codSitio", codSitio != null ? codSitio : "");
        props.setProperty("keyFactibilidad", keyFactibilidad != null ? keyFactibilidad : "");
        props.setProperty("keyCaja", keyCaja != null ? keyCaja : "");
        props.setProperty("puertoDisponible", String.valueOf(puertoDisponible));
        props.setProperty("codServicio", codServicio != null ? codServicio : "");
        props.setProperty("puertoReservado", String.valueOf(puertoReservado));
        props.setProperty("reservaDesde", reservaDesde != null ? reservaDesde : "");
        props.setProperty("reservaHasta", reservaHasta != null ? reservaHasta : "");
        props.setProperty("estadoReserva", estadoReserva != null ? estadoReserva : "");

        try {
            File cacheFile = new File(CACHE_FILE);
            try (OutputStream out = new FileOutputStream(cacheFile)) {
                props.store(out, "TestDataCache Persistence");
                System.out.println("✅ Datos GUARDADOS en cache: " + cacheFile.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("❌ Error guardando cache: " + e.getMessage());
        }
    }

    /**
     * Carga el estado previo del cache
     */
    public static synchronized void loadCache() {
        File cacheFile = new File(CACHE_FILE);
        if (!cacheFile.exists()) {
            System.out.println("⚠️ No existe archivo de cache previo");
            return;
        }

        Properties props = new Properties();
        try (InputStream in = new FileInputStream(cacheFile)) {
            props.load(in);

            // Cargar todas las variables
            codSitio = getPropertyOrNull(props, "codSitio");
            keyFactibilidad = getPropertyOrNull(props, "keyFactibilidad");
            keyCaja = getPropertyOrNull(props, "keyCaja");
            puertoDisponible = getPropertyAsInt(props, "puertoDisponible");
            codServicio = getPropertyOrNull(props, "codServicio");
            puertoReservado = getPropertyAsInt(props, "puertoReservado");
            reservaDesde = getPropertyOrNull(props, "reservaDesde");
            reservaHasta = getPropertyOrNull(props, "reservaHasta");
            estadoReserva = getPropertyOrNull(props, "estadoReserva");

            System.out.println("✅ Datos CARGADOS desde cache");
        } catch (IOException e) {
            System.err.println("❌ Error cargando cache: " + e.getMessage());
        }
    }

    /**
     * Limpia el cache en memoria y el archivo
     */
    public static synchronized void reset() {
        // Resetear variables
        codSitio = null;
        keyFactibilidad = null;
        keyCaja = null;
        puertoDisponible = 0;
        codServicio = null;
        puertoReservado = 0;
        reservaDesde = null;
        reservaHasta = null;
        estadoReserva = null;

        // Eliminar archivo
        File cacheFile = new File(CACHE_FILE);
        if (cacheFile.exists() && cacheFile.delete()) {
            System.out.println("♻️ Cache reseteado correctamente");
        }
    }

    // ================= HELPERS ================= //

    private static String getPropertyOrNull(Properties props, String key) {
        String value = props.getProperty(key);
        return (value == null || value.isEmpty()) ? null : value;
    }

    private static int getPropertyAsInt(Properties props, String key) {
        try {
            return Integer.parseInt(props.getProperty(key, "0"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}