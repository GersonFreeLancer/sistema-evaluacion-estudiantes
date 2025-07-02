package org.example.controller;
import org.example.dao.PostulanteDAO;
import org.example.dao.ResultadoExamenDAO;
import org.example.models.dto.DatosPostulante;
import org.example.models.Respuestas;
import org.example.models.ResultadoExamen;
import org.example.service.EvaluatorsService;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class EvaluacionController {

    private static final String CLAVE_OFICIAL = "ABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCDABCD";
    private final PostulanteDAO postulanteDAO = new PostulanteDAO();
    private final ResultadoExamenDAO resultadoExamenDAO = new ResultadoExamenDAO();

    public void evaluarPostulantes() {
        try {
            System.out.println("Obteniendo postulantes con respuestas...");
            List<DatosPostulante> datos = postulanteDAO.obtenerPostulantesConRespuestas();

            System.out.println("Iniciando evaluación paralela...");
            List<ResultadoExamen> resultados = datos
                    .parallelStream()
                    .map(dato -> {
                        Respuestas r = new Respuestas(dato.getCodigo(), dato.getRespuestas());
                        return EvaluatorsService.calcularResultado(r, CLAVE_OFICIAL);
                    })
                    .collect(Collectors.toList());

            System.out.println("Insertando resultados en lote...");
            resultadoExamenDAO.insertarLote(resultados);

            System.out.println("Evaluación completada exitosamente. Total evaluados: " + resultados.size());

        } catch (SQLException e) {
            System.err.println("Error durante la evaluación: " + e.getMessage());
            e.printStackTrace();
        }
    }
}