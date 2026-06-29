package com.peluqueria.agenda;

import com.peluqueria.agenda.Model.Agenda;
import com.peluqueria.agenda.Repository.AgendaRepository;
import com.peluqueria.agenda.Service.AgendaService;
import com.peluqueria.agenda.dto.AgendaResponseDTO;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendaServiceTest {

    @Mock AgendaRepository repository;
    @Mock WebClient webClientDisponibilidad;
    @Mock WebClient webClientTipoServicio;
    @Mock WebClient webClientCliente;
    @InjectMocks AgendaService service;

    private final Faker faker = new Faker();
    private Agenda agendaFake;

    @BeforeEach
    void setUp() {
        agendaFake = new Agenda(
            faker.number().numberBetween(1, 1000),
            "2026-08-" + faker.number().numberBetween(1, 28),
            "09:00",
            faker.random().nextLong(1, 4),
            faker.numerify("########-#"),
            faker.numerify("########-#"),
            "Confirmado"
        );
    }

    @Test
    @DisplayName("obtenerTodas retorna lista de citas")
    void obtenerTodas_retornaLista() {
        when(repository.findAll()).thenReturn(List.of(agendaFake));
        List<AgendaResponseDTO> resultado = service.obtenerTodas();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEstadoCita()).isEqualTo("Confirmado");
    }

    @Test
    @DisplayName("obtenerPorId retorna cita existente")
    void obtenerPorId_existente_retornaCita() {
        when(repository.findById(agendaFake.getIdAgenda()))
            .thenReturn(Optional.of(agendaFake));
        Optional<AgendaResponseDTO> resultado = service.obtenerPorId(agendaFake.getIdAgenda());
        assertThat(resultado).isPresent();
    }

    @Test
    @DisplayName("buscarPorProfesional filtra por RUT")
    void buscarPorProfesional_filtrado() {
        when(repository.findByRutProfesional(agendaFake.getRutProfesional()))
            .thenReturn(List.of(agendaFake));
        List<AgendaResponseDTO> resultado =
            service.buscarPorProfesional(agendaFake.getRutProfesional());
        assertThat(resultado).isNotEmpty();
    }

    @Test
    @DisplayName("eliminar llama deleteById")
    void eliminar_llamaDelete() {
        doNothing().when(repository).deleteById(anyInt());
        service.eliminar(agendaFake.getIdAgenda());
        verify(repository).deleteById(agendaFake.getIdAgenda());
    }
}