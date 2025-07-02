package org.example.service;

import org.example.models.ResultadoExamen;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class EvaluatorsService {

    public static ResultadoExamen calcularResultado(DatosPostulante dato, String clave) {
        String respuestas = dato.getRespuesta();
        int correctas = 0, incorrectas = 0, nulas = 0;
        double puntaje = 0;

        for (int i = 0; i < clave.length(); i++) {
            char respuesta = respuestas.charAt(i);
            char correcta = clave.charAt(i);

            if (respuesta == '*') {
                nulas++;
            } else if (respuesta == correcta) {
                correctas++;
                puntaje += 20;
            } else {
                incorrectas++;
                puntaje -= 1.275;
            }
        }

        ResultadoExamen resultado = new ResultadoExamen();
        resultado.setCodigoPostulante(dato.getCodigo());
        resultado.setPuntaje(Math.round(puntaje * 100.0) / 100.0);
        resultado.setMerito(""); // puedes calcular después por ordenamiento
        resultado.setRespuestasCorrectas(correctas);
        resultado.setRespuestasIncorrectas(incorrectas);
        resultado.setRespuestasNulas(nulas);
        resultado.setObservacion(ResultadoExamen.ObservacionEnum.fromPuntaje(puntaje));
        resultado.setFechaEvaluacion(java.time.LocalDateTime.now());

        return resultado;
    }
}