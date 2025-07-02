package org.example.controller;

import org.example.dao.PostulanteDAO;
import org.example.dao.RespuestasDAO;
import org.example.models.Postulante;
import org.example.models.Respuestas;
import org.example.service.GeneratorService;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class GeneratorController {
    private final PostulanteDAO postulanteDAO = new PostulanteDAO();
    private final RespuestasDAO respuestasDAO = new RespuestasDAO();

    public void generarRespuestasParaTodos() {
        try {
            System.out.println("🔍 Obteniendo postulantes...");
            List<Postulante> postulantes = postulanteDAO.obtenerTodos();

            System.out.println("⚙️ Generando respuestas aleatorias de forma paralela...");
            List<Respuestas> respuestas = postulantes
                    .parallelStream()
                    .map(p -> new Respuestas(p.getCodigoPostulante(), GeneratorService.generarRespuestasAleatorias()))
                    .collect(Collectors.toList());

            System.out.println("Insertando respuestas en la base de datos...");
            respuestasDAO.insertarLote(respuestas);

            System.out.println("✅ Generación completada exitosamente. Total generadas: " + respuestas.size());

        } catch (SQLException e) {
            System.err.println("Error durante la generación de respuestas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
