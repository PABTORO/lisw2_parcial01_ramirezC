package co.edu.unicauca.saberpro.pipeline;

import co.edu.unicauca.saberpro.model.Pregunta;

public class FiltroLongitudEnunciado implements IFiltroPregunta {
    @Override
    public boolean procesar(Pregunta pregunta) {
        if (pregunta == null) return false;
        // CORRECCIÓN 1: la condición estaba invertida (>= 15 retornaba false).
        // La regla exige que el enunciado tenga al menos 15 caracteres para ser válido.
        if (pregunta.getEnunciado() == null || pregunta.getEnunciado().length() < 15) {
            return false;
        }
        return true;
    }
}
