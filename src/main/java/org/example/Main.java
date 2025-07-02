// TestDAOsMain.java - Clase para probar todos los DAOs
package org.example;

import org.example.dao.*;
import org.example.models.*;
import org.example.models.dto.*;
import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("=== INICIANDO PRUEBAS DE DAOs ===\n");

        // Probar cada DAO
        probarPostulanteDAO();
        probarRespuestasDAO();
        probarResultadoExamenDAO();
        probarEscuelaProfesionalDAO();
        probarAnioProcesoDAO();
        probarLocalDAO();
        probarDatosEvaluacionDAO();

        System.out.println("\n=== PRUEBAS COMPLETADAS ===");
    }

    // ============================================
    // PROBAR POSTULANTE DAO
    // ============================================
    private static void probarPostulanteDAO() {
        System.out.println("📋 PROBANDO PostulanteDAO...");

        try {
            PostulanteDAO postulanteDAO = new PostulanteDAO();

            // Obtener todos los postulantes
            List<Postulante> postulantes = postulanteDAO.obtenerTodos();
            System.out.println("   ✅ Total postulantes: " + postulantes.size());

            // Mostrar algunos postulantes
            if (!postulantes.isEmpty()) {
                System.out.println("   📄 Primeros postulantes:");
                for (int i = 0; i < Math.min(3, postulantes.size()); i++) {
                    Postulante p = postulantes.get(i);
                    System.out.printf("      - %s: %s (Escuela: %d)%n",
                            p.getCodigoPostulante(), p.getNombre(), p.getIdEscuela());
                }
            }

            // Probar postulantes con respuestas
            List<DatosPostulante> datosCompletos = postulanteDAO.obtenerPostulantesConRespuestas();
            System.out.println("   ✅ Postulantes con respuestas: " + datosCompletos.size());

            if (!datosCompletos.isEmpty()) {
                DatosPostulante dato = datosCompletos.get(0);
                System.out.printf("   📄 Ejemplo: %s - Respuestas: %s...%n",
                        dato.getCodigo(),
                        dato.getRespuestas().substring(0, Math.min(20, dato.getRespuestas().length())));
            }

        } catch (SQLException e) {
            System.out.println("   ❌ Error en PostulanteDAO: " + e.getMessage());
        }

        System.out.println();
    }

    // ============================================
    // PROBAR RESPUESTAS DAO
    // ============================================
    private static void probarRespuestasDAO() {
        System.out.println("📝 PROBANDO RespuestasDAO...");

        try {
            RespuestasDAO respuestasDAO = new RespuestasDAO();

            // Obtener todas las respuestas
            List<Respuestas> respuestas = respuestasDAO.obtenerTodas();
            System.out.println("   ✅ Total respuestas: " + respuestas.size());

            // Probar obtener por código específico
            if (!respuestas.isEmpty()) {
                String codigo = respuestas.get(0).getCodigoPostulante();
                Respuestas respuesta = respuestasDAO.obtenerPorCodigo(codigo);
                if (respuesta != null) {
                    System.out.printf("   ✅ Respuesta de %s: %d caracteres%n",
                            codigo, respuesta.getRespuesta().length());
                }
            }

            // Probar mapa de respuestas (optimizado para paralelismo)
            var mapaRespuestas = respuestasDAO.obtenerMapaRespuestas();
            System.out.println("   ✅ Mapa de respuestas: " + mapaRespuestas.size() + " entradas");

        } catch (SQLException e) {
            System.out.println("   ❌ Error en RespuestasDAO: " + e.getMessage());
        }

        System.out.println();
    }

    // ============================================
    // PROBAR RESULTADO EXAMEN DAO
    // ============================================
    private static void probarResultadoExamenDAO() {
        System.out.println("🎯 PROBANDO ResultadoExamenDAO...");

        try {
            ResultadoExamenDAO resultadoDAO = new ResultadoExamenDAO();

            // Limpiar resultados anteriores
            resultadoDAO.limpiarResultados();
            System.out.println("   🧹 Resultados anteriores limpiados");

            // Crear y insertar un resultado de prueba
            ResultadoExamen resultado = new ResultadoExamen();
            resultado.setCodigoPostulante("POST001");
            resultado.setPuntaje(85.5);
            resultado.setMerito("1");
            resultado.setObservacion(ResultadoExamen.ObservacionEnum.ALCANZANTE);
            resultado.setRespuestasCorrectas(45);
            resultado.setRespuestasIncorrectas(15);
            resultado.setRespuestasNulas(40);

            boolean insertado = resultadoDAO.insertar(resultado);
            System.out.println("   ✅ Resultado insertado: " + insertado);

            // Obtener top resultados
            List<ResultadoExamen> topResultados = resultadoDAO.obtenerTopResultados(5);
            System.out.println("   ✅ Top resultados: " + topResultados.size());

            for (ResultadoExamen res : topResultados) {
                System.out.printf("      - %s: %.2f puntos (Mérito: %s)%n",
                        res.getCodigoPostulante(), res.getPuntaje(), res.getMerito());
            }

        } catch (SQLException e) {
            System.out.println("   ❌ Error en ResultadoExamenDAO: " + e.getMessage());
        }

        System.out.println();
    }

    // ============================================
    // PROBAR ESCUELA PROFESIONAL DAO
    // ============================================
    private static void probarEscuelaProfesionalDAO() {
        System.out.println("🏫 PROBANDO EscuelaProfesionalDAO...");

        try {
            EscuelaProfesionalDAO escuelaDAO = new EscuelaProfesionalDAO();

            // Obtener todas las escuelas
            List<EscuelaProfesional> escuelas = escuelaDAO.obtenerTodas();
            System.out.println("   ✅ Total escuelas: " + escuelas.size());

            // Mostrar escuelas con sus locales
            for (EscuelaProfesional escuela : escuelas) {
                String nombreLocal = escuela.getLocal() != null ? escuela.getLocal().getNombre() : "Sin local";
                System.out.printf("      - %s (Local: %s)%n", escuela.getNombre(), nombreLocal);
            }

            // Probar obtener por ID
            if (!escuelas.isEmpty()) {
                int idPrimera = escuelas.get(0).getId();
                EscuelaProfesional escuela = escuelaDAO.obtenerPorId(idPrimera);
                if (escuela != null) {
                    System.out.printf("   ✅ Escuela por ID %d: %s%n", idPrimera, escuela.getNombre());
                }
            }

        } catch (SQLException e) {
            System.out.println("   ❌ Error en EscuelaProfesionalDAO: " + e.getMessage());
        }

        System.out.println();
    }

    // ============================================
    // PROBAR ANIO PROCESO DAO
    // ============================================
    private static void probarAnioProcesoDAO() {
        System.out.println("📅 PROBANDO AnioProcesoDAO...");

        try {
            AnioProcesoDAO anioDAO = new AnioProcesoDAO();

            // Obtener todos los procesos
            List<AnioProceso> procesos = anioDAO.obtenerTodos();
            System.out.println("   ✅ Total procesos: " + procesos.size());

            // Mostrar procesos
            for (AnioProceso proceso : procesos) {
                System.out.printf("      - %d: %s%n", proceso.getYear(), proceso.getProceso());
            }

            // Obtener proceso actual
            AnioProceso actual = anioDAO.obtenerActual();
            if (actual != null) {
                System.out.printf("   ✅ Proceso actual: %d - %s%n", actual.getYear(), actual.getProceso());
            } else {
                System.out.println("   ⚠️ No hay proceso actual definido");
            }

        } catch (SQLException e) {
            System.out.println("   ❌ Error en AnioProcesoDAO: " + e.getMessage());
        }

        System.out.println();
    }

    // ============================================
    // PROBAR LOCAL DAO
    // ============================================
    private static void probarLocalDAO() {
        System.out.println("🏢 PROBANDO LocalDAO...");

        try {
            LocalDAO localDAO = new LocalDAO();

            // Obtener todos los locales
            List<Local> locales = localDAO.obtenerTodos();
            System.out.println("   ✅ Total locales: " + locales.size());

            // Mostrar locales
            for (Local local : locales) {
                System.out.printf("      - ID %d: %s%n", local.getId(), local.getNombre());
            }

            // Probar obtener por ID
            if (!locales.isEmpty()) {
                int idPrimero = locales.get(0).getId();
                Local local = localDAO.obtenerPorId(idPrimero);
                if (local != null) {
                    System.out.printf("   ✅ Local por ID %d: %s%n", idPrimero, local.getNombre());
                }
            }

        } catch (SQLException e) {
            System.out.println("   ❌ Error en LocalDAO: " + e.getMessage());
        }

        System.out.println();
    }

    // ============================================
    // PROBAR DATOS EVALUACION DAO
    // ============================================
    private static void probarDatosEvaluacionDAO() {
        System.out.println("⚡ PROBANDO DatosEvaluacionDAO...");

        try {
            DatosEvaluacionDAO datosDAO = new DatosEvaluacionDAO();

            // Obtener datos completos para evaluación
            List<DatosPostulante> datosCompletos = datosDAO.obtenerDatosCompletos();
            System.out.println("   ✅ Datos completos para evaluación: " + datosCompletos.size());

            // Mostrar ejemplo de datos
            if (!datosCompletos.isEmpty()) {
                DatosPostulante ejemplo = datosCompletos.get(0);
                System.out.printf("   📄 Ejemplo: %s - %s (Escuela: %s)%n",
                        ejemplo.getCodigo(), ejemplo.getNombre(), ejemplo.getNombreEscuela());
                System.out.printf("      Respuestas: %s...%n",
                        ejemplo.getRespuestas().substring(0, Math.min(30, ejemplo.getRespuestas().length())));
            }

            // Probar datos por escuela
            if (!datosCompletos.isEmpty()) {
                int idEscuela = datosCompletos.get(0).getIdEscuela();
                List<DatosPostulante> porEscuela = datosDAO.obtenerPorEscuela(idEscuela);
                System.out.printf("   ✅ Datos de escuela %d: %d postulantes%n", idEscuela, porEscuela.size());
            }

            // Obtener gabarito
            String gabarito = datosDAO.obtenerGabarito(1);
            System.out.printf("   ✅ Gabarito: %s... (%d caracteres)%n",
                    gabarito.substring(0, Math.min(20, gabarito.length())), gabarito.length());

        } catch (SQLException e) {
            System.out.println("   ❌ Error en DatosEvaluacionDAO: " + e.getMessage());
        }

        System.out.println();
    }

    // ============================================
    // PRUEBA DE RENDIMIENTO (OPCIONAL)
    // ============================================
    private static void pruebaRendimiento() {
        System.out.println("⏱️ PRUEBA DE RENDIMIENTO...");

        try {
            long inicio = System.currentTimeMillis();

            DatosEvaluacionDAO datosDAO = new DatosEvaluacionDAO();
            List<DatosPostulante> datos = datosDAO.obtenerDatosCompletos();

            long tiempo = System.currentTimeMillis() - inicio;

            System.out.printf("   ✅ Cargados %d registros en %d ms%n", datos.size(), tiempo);
            System.out.printf("   📊 Rendimiento: %.2f registros/ms%n", (double) datos.size() / tiempo);

        } catch (SQLException e) {
            System.out.println("   ❌ Error en prueba de rendimiento: " + e.getMessage());
        }

        System.out.println();
    }

    // ============================================
    // MÉTODO AUXILIAR PARA ESTADÍSTICAS
    // ============================================
    private static void mostrarEstadisticas() {
        System.out.println("📊 ESTADÍSTICAS GENERALES...");

        try {
            PostulanteDAO postulanteDAO = new PostulanteDAO();
            RespuestasDAO respuestasDAO = new RespuestasDAO();
            EscuelaProfesionalDAO escuelaDAO = new EscuelaProfesionalDAO();

            int totalPostulantes = postulanteDAO.obtenerTodos().size();
            int totalRespuestas = respuestasDAO.obtenerTodas().size();
            int totalEscuelas = escuelaDAO.obtenerTodas().size();

            System.out.println("   📋 Total postulantes: " + totalPostulantes);
            System.out.println("   📝 Total respuestas: " + totalRespuestas);
            System.out.println("   🏫 Total escuelas: " + totalEscuelas);

            if (totalPostulantes > 0 && totalRespuestas > 0) {
                double porcentajeCompleto = (double) totalRespuestas / totalPostulantes * 100;
                System.out.printf("   ✅ Completitud: %.1f%% de postulantes con respuestas%n", porcentajeCompleto);
            }

        } catch (SQLException e) {
            System.out.println("   ❌ Error al obtener estadísticas: " + e.getMessage());
        }

        System.out.println();
    }
}

// ============================================
// MAIN SIMPLIFICADO (ALTERNATIVA RÁPIDA)
// ============================================

/*
public class TestDAOsMain {
    public static void main(String[] args) {
        
        System.out.println("=== PRUEBA RÁPIDA DE DAOs ===\n");
        
        // Solo probar lo esencial
        try {
            // 1. Probar PostulanteDAO
            PostulanteDAO postulanteDAO = new PostulanteDAO();
            int totalPostulantes = postulanteDAO.obtenerTodos().size();
            System.out.println("✅ PostulanteDAO: " + totalPostulantes + " postulantes");
            
            // 2. Probar DatosPostulante
            int datosCompletos = postulanteDAO.obtenerPostulantesConRespuestas().size();
            System.out.println("✅ Datos completos: " + datosCompletos + " registros");
            
            // 3. Probar EscuelaProfesionalDAO
            EscuelaProfesionalDAO escuelaDAO = new EscuelaProfesionalDAO();
            int totalEscuelas = escuelaDAO.obtenerTodas().size();
            System.out.println("✅ EscuelaProfesionalDAO: " + totalEscuelas + " escuelas");
            
            System.out.println("\n🎉 Todos los DAOs funcionan correctamente!");
            
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
*/