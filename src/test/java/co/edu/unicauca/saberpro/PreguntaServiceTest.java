package co.edu.unicauca.saberpro;

import static org.junit.jupiter.api.Assertions.*;

import co.edu.unicauca.saberpro.model.Pregunta;
import co.edu.unicauca.saberpro.pipeline.FiltroEnunciado;
import co.edu.unicauca.saberpro.pipeline.FiltroLongitudEnunciado;
import co.edu.unicauca.saberpro.pipeline.ValidadorPipeline;
import co.edu.unicauca.saberpro.repository.IPreguntaRepository;
import co.edu.unicauca.saberpro.repository.PreguntaListRepository;
import co.edu.unicauca.saberpro.service.GestorPreguntaServiceImpl;
import co.edu.unicauca.saberpro.service.IPreguntaService;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PreguntaServiceTest {

    private IPreguntaRepository repository;
    private IPreguntaService service;

    @BeforeEach
    void setUp() {
        repository = new PreguntaListRepository();
        ValidadorPipeline pipeline = new ValidadorPipeline();
        pipeline.addFiltro(new FiltroEnunciado());
        pipeline.addFiltro(new FiltroLongitudEnunciado());
        service = new GestorPreguntaServiceImpl(repository, pipeline);
    }

    private Pregunta crear(int id, String enunciado) {
        return new Pregunta(id, enunciado,
                Arrays.asList("A. Uno", "B. Dos", "C. Tres", "D. Cuatro"),
                "A. Uno", "BORRADOR", 85);
    }

    @Test
    void registraPreguntaValida() {
        assertTrue(service.registrarPregunta(crear(1, "¿Qué principio SOLID separa responsabilidades?")));
        assertEquals(1, service.obtenerTodas().size());
    }

    @Test
    void laPreguntaPersisteEnElRepositorio() {
        service.registrarPregunta(crear(7, "¿Qué es el patrón Tuberías y Filtros?"));
        assertNotNull(repository.buscarPorId(7));
    }

    @Test
    void rechazaEnunciadoVacio() {
        assertFalse(service.registrarPregunta(crear(2, "   ")));
        assertEquals(0, service.obtenerTodas().size());
    }

    @Test
    void rechazaEnunciadoNulo() {
        assertFalse(service.registrarPregunta(crear(3, null)));
    }

    @Test
    void rechazaEnunciadoMenorA15Caracteres() {
        assertFalse(service.registrarPregunta(crear(4, "¿Qué es DIP?")));
        assertEquals(0, service.obtenerTodas().size());
    }

    @Test
    void aceptaEnunciadoDeExactamente15Caracteres() {
        assertTrue(service.registrarPregunta(crear(5, "123456789012345")));
    }

    // PUNTO 2 - Opción 08: Control de Límite de Banco

    @Test
    void permiteRegistrarHastaCincoPreguntas() {
        for (int id = 1; id <= 5; id++) {
            assertTrue(service.registrarPregunta(crear(id, "¿Pregunta válida número " + id + "?")));
        }
        assertEquals(5, service.obtenerTodas().size());
    }

    @Test
    void rechazaLaSextaPregunta() {
        for (int id = 1; id <= 5; id++) {
            service.registrarPregunta(crear(id, "¿Pregunta válida número " + id + "?"));
        }
        assertFalse(service.registrarPregunta(crear(6, "¿Sexta pregunta que supera el límite?")));
        assertEquals(5, service.obtenerTodas().size());
        assertNull(repository.buscarPorId(6));
    }

    @Test
    void unaPreguntaInvalidaNoOcupaCupo() {
        assertFalse(service.registrarPregunta(crear(1, "corta")));
        for (int id = 2; id <= 6; id++) {
            assertTrue(service.registrarPregunta(crear(id, "¿Pregunta válida número " + id + "?")));
        }
        assertEquals(5, service.obtenerTodas().size());
    }
}
