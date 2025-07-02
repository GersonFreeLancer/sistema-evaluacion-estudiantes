package org.Admision.SanMarcos;

import org.Admision.SanMarcos.service.EvaluatorsService;
import org.Admision.SanMarcos.service.GeneratorService;

public class Main {
    public static void main(String[] args) {
        String respuesta = "ACB*CDBC*CAADB**AA*ACCBBBDC*D***CDDDBC*DCDCABAAADDCBCCABDBDBADDDDBA*C*D*DACD*ADB*DABCDB**ABCDBCDABB*";
        String respuestas=GeneratorService.generarRespuestasAleatorias();
        float puntaje=EvaluatorsService.calcularPuntaje(respuestas, respuesta);
        System.out.println("Respuestas: "+respuestas);
        System.out.println("Puntaje: "+puntaje);
    }
}