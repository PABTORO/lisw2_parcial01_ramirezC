package co.edu.unicauca.saberpro.repository;

import co.edu.unicauca.saberpro.model.Pregunta;
import java.util.ArrayList;
import java.util.List;

public class PreguntaListRepository implements IPreguntaRepository {
    private List<Pregunta> bancoPreguntas = new ArrayList<>();

    @Override
    public boolean guardar(Pregunta pregunta) {
        // CORRECCIÓN 2: se eliminó la lista local que ocultaba (shadowing) al atributo
        // de la clase; la pregunta se guardaba en una lista temporal y se perdía.
        return bancoPreguntas.add(pregunta);
    }

    @Override
    public List<Pregunta> listar() {
        return bancoPreguntas;
    }

    @Override
    public Pregunta buscarPorId(int id) {
        return bancoPreguntas.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
