package co.edu.unicauca.saberpro.service;

import co.edu.unicauca.saberpro.model.Pregunta;
import co.edu.unicauca.saberpro.pipeline.ValidadorPipeline;
import co.edu.unicauca.saberpro.repository.IPreguntaRepository;
import java.util.List;

public class GestorPreguntaServiceImpl implements IPreguntaService {
    // PUNTO 2 - Opción 08: límite máximo de preguntas permitidas en el banco.
    public static final int LIMITE_BANCO = 5;

    private final IPreguntaRepository repository;
    private final ValidadorPipeline pipeline;

    public GestorPreguntaServiceImpl(IPreguntaRepository repository, ValidadorPipeline pipeline) {
        this.repository = repository;
        this.pipeline = pipeline;
    }

    // CORRECCIÓN 3: el método se llamaba procesarYGuardarPregunta y no cumplía el
    // contrato de IPreguntaService (no compilaba). Se renombra a registrarPregunta
    // con @Override y se elimina el texto suelto "IPreguntaService".
    @Override
    public boolean registrarPregunta(Pregunta pregunta) {
        // PUNTO 2 - Opción 08: regla de negocio del servicio. Si el banco ya tiene
        // 5 preguntas, se rechaza el registro con una advertencia y se retorna false.
        if (repository.listar().size() >= LIMITE_BANCO) {
            System.out.println("ADVERTENCIA: el banco alcanzó el límite máximo de "
                    + LIMITE_BANCO + " preguntas. Registro rechazado.");
            return false;
        }
        if (pipeline.ejecutar(pregunta)) {
            return repository.guardar(pregunta);
        }
        return false;
    }

    @Override
    public List<Pregunta> obtenerTodas() {
        return repository.listar();
    }
}
