package MarcaBlanca.CapadeIntegracion;

import org.testng.annotations.Test;
import utilities.TestDataCache;
import java.io.File;

public class CacheVerificationTest {

    @Test(groups = {"debug", "cache"})
    public void quickCacheCheck() {
        System.out.println("\n=== VERIFICACIÓN RÁPIDA DEL CACHE ===");

        // 1. Mostrar ubicación del archivo de cache
        String cachePath = System.getProperty("java.io.tmpdir") + "/testdata_cache.properties";
        System.out.println("Ubicación archivo cache: " + new File(cachePath).getAbsolutePath());

        // 2. Mostrar estado actual en memoria
        System.out.println("\n[Estado ACTUAL en memoria]");
        System.out.println("Código Sitio: " + TestDataCache.codSitio);
        System.out.println("Key Factibilidad: " + TestDataCache.keyFactibilidad);;
        System.out.println("Key Caja: " + TestDataCache.keyCaja);;
        System.out.println("Código Servicio: " + TestDataCache.codServicio);
        System.out.println("Puerto Reservado: " + TestDataCache.puertoReservado);
        System.out.println("Reserva Desde-Hasta: " + TestDataCache.reservaDesde + " a " + TestDataCache.reservaHasta);

        // 3. Forzar recarga desde disco
        TestDataCache.loadCache();

        // 4. Mostrar estado después de recargar
        System.out.println("\n[Estado DESPUÉS de recargar desde disco]");
        System.out.println("Código Sitio: " + TestDataCache.codSitio);
        System.out.println("Key Factibilidad: " + TestDataCache.keyFactibilidad);
        System.out.println("Key Caja: " + TestDataCache.keyCaja);
        System.out.println("Código Servicio: " + TestDataCache.codServicio);
        System.out.println("Puerto Reservado: " + TestDataCache.puertoReservado);
        System.out.println("Reserva Desde-Hasta: " + TestDataCache.reservaDesde + " a " + TestDataCache.reservaHasta);

        // 5. Verificar existencia del archivo
        File cacheFile = new File(cachePath);
        System.out.println("\n[Estado del ARCHIVO]");
        System.out.println("Existe: " + cacheFile.exists());
        if (cacheFile.exists()) {
            System.out.println("Tamaño: " + cacheFile.length() + " bytes");
            System.out.println("Última modificación: " + new java.util.Date(cacheFile.lastModified()));
        }
    }
}