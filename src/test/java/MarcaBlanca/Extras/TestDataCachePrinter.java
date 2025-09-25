package MarcaBlanca.CapadeIntegracion;

import utilities.TestDataCache;

public class TestDataCachePrinter {

    public static void printFullCache() {
        System.out.println("\n══════════════════════════════════════════════════");
        System.out.println("           CONTENIDO ACTUAL DE TEST DATA CACHE     ");
        System.out.println("══════════════════════════════════════════════════");

        printCacheSection("DATOS DE SITIO Y CLAVES",
                "Código Sitio", TestDataCache.codSitio,
                "Key Factibilidad", TestDataCache.keyFactibilidad,
                "Key Caja", TestDataCache.keyCaja);

        printCacheSection("DATOS DE PUERTOS",
                "Puerto Disponible", TestDataCache.puertoDisponible,
                "Puerto Reservado", TestDataCache.puertoReservado);

        printCacheSection("DATOS DE RESERVA",
                "Código Servicio", TestDataCache.codServicio,
                "Reserva Desde", TestDataCache.reservaDesde,
                "Reserva Hasta", TestDataCache.reservaHasta,
                "Estado Reserva", TestDataCache.estadoReserva);

        System.out.println("══════════════════════════════════════════════════\n");
    }

    private static void printCacheSection(String sectionTitle, Object... items) {
        System.out.println("\n▬▬▬ " + sectionTitle + " ▬▬▬");
        for (int i = 0; i < items.length; i += 2) {
            String key = items[i].toString();
            Object value = items[i + 1];
            System.out.printf("  %-20s: %s%n", key, value != null ? value : "null");
        }
    }

    public static void printCacheForActivacion() {
        System.out.println("\n[INFORMACIÓN RELEVANTE PARA ACTIVACIÓN]");
        System.out.printf("  %-20s: %s%n", "Código Servicio", TestDataCache.codServicio);
        System.out.printf("  %-20s: %d%n", "Puerto Reservado", TestDataCache.puertoReservado);
        System.out.printf("  %-20s: %s%n", "Estado Reserva", TestDataCache.estadoReserva);
    }
}