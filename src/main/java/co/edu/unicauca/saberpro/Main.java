package co.edu.unicauca.saberpro;

import co.edu.unicauca.saberpro.model.Pregunta;
import co.edu.unicauca.saberpro.pipeline.*;
import co.edu.unicauca.saberpro.repository.*;
import co.edu.unicauca.saberpro.service.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== BANCO DE PREGUNTAS SABER PRO (GRUPO B) ===");

        IPreguntaRepository repository = new PreguntaListRepository();
        ValidadorPipeline pipeline = new ValidadorPipeline();
        pipeline.addFiltro(new FiltroEnunciado());
        pipeline.addFiltro(new FiltroLongitudEnunciado());

        // CORRECCIÓN 4: se inyectaba null en lugar del pipeline (NullPointerException
        // al registrar). Se inyecta el pipeline ya configurado.
        IPreguntaService service = new GestorPreguntaServiceImpl(repository, pipeline);

        Pregunta p1 = new Pregunta(
            1,
            "¿Cuál es la función principal del patrón Microkernel para agregar módulos sin alterar el núcleo?",
            Arrays.asList("A. Plugins dinámicos", "B. Monolito rígido", "C. Base de datos", "D. Redes"),
            "A. Plugins dinámicos",
            "BORRADOR",
            85
        );

        boolean exito = service.registrarPregunta(p1);
        if (exito) {
            System.out.println(" Pregunta registrada con éxito. Total en banco: " + service.obtenerTodas().size());
        } else {
            System.out.println(" No se pudo registrar la pregunta debido a fallos en el pipeline.");
        }

        // PUNTO 2 - Opción 08: demostración del control de límite del banco.
        // Se intentan registrar las preguntas 2 a 6; la 6ta debe ser rechazada.
        System.out.println("--- Opción 08: Control de Límite de Banco (máximo 5) ---");
        for (int id = 2; id <= 6; id++) {
            Pregunta p = new Pregunta(
                id,
                "¿Pregunta de prueba número " + id + " sobre arquitectura de software?",
                Arrays.asList("A. Opción uno", "B. Opción dos", "C. Opción tres", "D. Opción cuatro"),
                "A. Opción uno",
                "BORRADOR",
                50
            );
            boolean registrada = service.registrarPregunta(p);
            System.out.println(" Pregunta " + id + (registrada ? " registrada" : " RECHAZADA")
                    + ". Total en banco: " + service.obtenerTodas().size());
        }
    }
}
