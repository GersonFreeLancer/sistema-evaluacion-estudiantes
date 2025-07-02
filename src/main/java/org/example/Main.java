package org.example;

import org.example.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== PROBANDO CONEXIÓN A BASE DE DATOS ===\n");

        // 1. Probar conexión básica
        probarConexionBasica();

        // 2. Probar consulta simple
        probarConsultaSimple();

        // 3. Probar todas las tablas
        probarTodasLasTablas();

        System.out.println("\n=== PRUEBAS COMPLETADAS ===");
    }

    // Método 1: Probar conexión básica
    private static void probarConexionBasica() {
        System.out.println("1. Probando conexión básica...");

        DatabaseConnection dbConn = DatabaseConnection.getInstance();

        if (dbConn.testConnection()) {
            System.out.println("✅ Conexión exitosa!");
        } else {
            System.out.println("❌ Error en la conexión");
            return;
        }

        System.out.println();
    }

    // Método 2: Probar consulta simple
    private static void probarConsultaSimple() {
        System.out.println("2. Probando consulta simple...");

        Connection connection = null;
        try {
            connection = DatabaseConnection.getInstance().getConnection();

            String sql = "SELECT COUNT(*) as total FROM postulante";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total");
                System.out.println("✅ Total de postulantes: " + total);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error en consulta: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }

        System.out.println();
    }

    // Método 3: Probar todas las tablas
    private static void probarTodasLasTablas() {
        System.out.println("3. Verificando todas las tablas...");

        String[] tablas = {
                "anio_proceso",
                "local",
                "escuela_profesional",
                "postulante",
                "respuestas",
                "resultado_examen"
        };

        for (String tabla : tablas) {
            contarRegistrosTabla(tabla);
        }

        System.out.println();
    }

    // Método auxiliar para contar registros por tabla
    private static void contarRegistrosTabla(String nombreTabla) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getInstance().getConnection();

            String sql = "SELECT COUNT(*) as total FROM " + nombreTabla;
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total");
                System.out.printf("   - %-20s: %d registros%n", nombreTabla, total);
            }

        } catch (SQLException e) {
            System.out.printf("   - %-20s: ❌ Error - %s%n", nombreTabla, e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
    }

    // Método adicional: Mostrar algunos postulantes
    private static void mostrarPostulantes() {
        System.out.println("4. Mostrando algunos postulantes...");

        Connection connection = null;
        try {
            connection = DatabaseConnection.getInstance().getConnection();

            String sql = "SELECT codigo_postulante, nombre, idescuela FROM postulante LIMIT 5";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            System.out.println("   Código    | Nombre                 | ID Escuela");
            System.out.println("   ----------|------------------------|----------");

            while (rs.next()) {
                String codigo = rs.getString("codigo_postulante");
                String nombre = rs.getString("nombre");
                int idEscuela = rs.getInt("idescuela");

                System.out.printf("   %-9s | %-22s | %d%n",
                        codigo,
                        nombre.length() > 22 ? nombre.substring(0, 22) : nombre,
                        idEscuela);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al mostrar postulantes: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }

        System.out.println();
    }
}