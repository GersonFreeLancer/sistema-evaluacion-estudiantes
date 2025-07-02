package org.Admision.SanMarcos;

import org.Admision.SanMarcos.service.GeneratorService;

public class Main {
    public static void main(String[] args) {
        String respuestas=GeneratorService.generarRespuestasAleatorias();
        System.out.println("Respuestas: "+respuestas);
    }
}