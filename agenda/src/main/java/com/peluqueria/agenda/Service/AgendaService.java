package com.peluqueria.agenda.Service;

import com.peluqueria.agenda.dto.*;
import com.peluqueria.agenda.Model.Agenda;
import com.peluqueria.agenda.Repository.AgendaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgendaService {

    private final AgendaRepository repository;
    private final WebClient webClientDisponibilidad;
    private final WebClient webClientTipoServicio;
    private final WebClient webClientCliente;

    private AgendaResponseDTO mapToDTO(Agenda a) {
        return new AgendaResponseDTO(
            a.getIdAgenda(), 
            a.getFecha(), 
            a.getHoraInicio(),
            a.getIdTipoServicio(), 
            a.getRutProfesional(), 
            a.getRutCliente(), 
            a.getEstadoCita()
            );
    }

    // ── VALIDACIONES ─────────────────────────────────────────
    private void validarDisponibilidad(String rutProfesional, String fecha, String horaInicio) {

        // 1. Validar contra el repositorio local primero
        boolean ocupado = repository.existsByRutProfesionalAndFechaAndHoraInicio(rutProfesional, fecha, horaInicio);
        if (ocupado) {
            throw new RuntimeException("El profesional ya tiene una reserva en ese horario");
        }

        // 2. Validar contra el microservicio externo
        try {
            webClientDisponibilidad.get()
                .uri("/api/disponibilidadProfesional/profesional/{rut}/fechayHora/{fecha}/{hora}",
                    rutProfesional, fecha, horaInicio)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        } catch (WebClientResponseException.NotFound e) {
            throw new RuntimeException(
                "El profesional no tiene disponibilidad para la fecha y hora solicitadas.");
        } catch (WebClientResponseException e) {
            throw new RuntimeException(
                "Error al consultar disponibilidad: " + e.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException(
                "No se puede conectar con api Disponibilidad: " + e.getMessage());
        }
    }

    private void validarTipoServicio(Long idTipoServicio) {

        try {
            webClientTipoServicio.get()
                    .uri("/api/tipoServicio/{id}", idTipoServicio)
                    .retrieve()
                    .bodyToMono(Long.class)
                    .block();
            log.info(">>> Tipo de servicio {} validado correctamente", idTipoServicio);

        } catch (WebClientResponseException.NotFound e) {
            throw new RuntimeException(
                    "El tipo de servicio con id " + idTipoServicio + " no existe.");
        } catch (Exception e) {
            throw new RuntimeException(
                    "No se puede conectar con Tipo de Servicio: " + e.getMessage());
        }
    }

        private void validarCliente(String rutCliente) {

        try {
            webClientCliente.get()
                    .uri("/api/cliente/{rut}", rutCliente)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info(">>> Cliente {} validado correctamente", rutCliente);

        } catch (WebClientResponseException.NotFound e) {
            throw new RuntimeException(
                    "El cliente con rut " + rutCliente + " no existe.");
        } catch (Exception e) {
            throw new RuntimeException(
                    "No se puede conectar con Cliente: " + e.getMessage());
        }
    }


    // ── CRUD ─────────────────────────────────────────
    public List<AgendaResponseDTO> obtenerTodas() {
        return repository.findAll().stream()
                .map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<AgendaResponseDTO> obtenerPorId(Integer idAgenda) {
        return repository.findById(idAgenda).map(this::mapToDTO);
    }

    public List<AgendaResponseDTO> buscarPorProfesional(String rutProfesional) {
        return repository.findByRutProfesional(rutProfesional)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<AgendaResponseDTO> buscarPorCliente(String rutCliente) {
        return repository.findByRutCliente(rutCliente)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<AgendaResponseDTO> buscarPorFecha(String fecha) {
        return repository.findByFecha(fecha)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }



    public AgendaResponseDTO guardar(AgendaRequestDTO dto) {
        validarDisponibilidad(dto.getRutProfesional(), dto.getFecha(), dto.getHoraInicio());
        validarTipoServicio(dto.getIdTipoServicio());
        validarCliente(dto.getRutCliente());

        Agenda agenda = new Agenda(
            null,
            dto.getFecha(),
            dto.getHoraInicio(),
            dto.getIdTipoServicio(),
            dto.getRutProfesional(),
            dto.getRutCliente(),
            dto.getEstadoCita()
        );
        return mapToDTO(repository.save(agenda));
    }

    public Optional<AgendaResponseDTO> actualizar(Integer idAgenda, AgendaRequestDTO dto) {
        return repository.findById(idAgenda).map(existente -> {
            validarDisponibilidad(dto.getRutProfesional(), dto.getFecha(), dto.getHoraInicio());
            existente.setFecha(dto.getFecha());
            existente.setHoraInicio(dto.getHoraInicio());
            existente.setIdTipoServicio(dto.getIdTipoServicio());
            existente.setRutProfesional(dto.getRutProfesional());
            existente.setRutCliente(dto.getRutCliente());
            existente.setEstadoCita(dto.getEstadoCita());
            return mapToDTO(repository.save(existente));
        });
    }

    public void eliminar(Integer idAgenda) {
        repository.deleteById(idAgenda);
    }

}
