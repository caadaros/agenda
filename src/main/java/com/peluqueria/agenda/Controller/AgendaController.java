package com.peluqueria.agenda.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.peluqueria.agenda.Service.AgendaService;
import com.peluqueria.agenda.dto.AgendaRequestDTO;
import com.peluqueria.agenda.dto.AgendaResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/agenda")
@RequiredArgsConstructor
@Validated
@Tag(name = "Agenda", description = "Controlador para gestionar la agenda de citas")
public class AgendaController {
    private final AgendaService service;

    @GetMapping
    @Operation(summary = "Obtener todas las citas", description = "Recupera una lista de todas las citas en la agenda")
    @ApiResponse(responseCode = "200", description = "Lista de citas obtenida correctamente")
    public ResponseEntity<List<AgendaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(service.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cita por ID", description = "Recupera una cita específica por su ID")
    @ApiResponse(responseCode = "200", description = "Cita obtenida correctamente")
    @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    public ResponseEntity<AgendaResponseDTO> obtenerPorId(@PathVariable ("id") Integer idAgenda) {
        return service.obtenerPorId(idAgenda)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/profesional/{rutProfesional}")
    @Operation(summary = "Obtener citas por profesional", description = "Recupera una lista de citas para un profesional específico")
    @ApiResponse(responseCode = "200", description = "Lista de citas obtenida correctamente")
    @ApiResponse(responseCode = "404", description = "Profesional no encontrado")
    public ResponseEntity<List<AgendaResponseDTO>> porProfesional(@PathVariable ("rutProfesional") String rutProfesional) {
        return ResponseEntity.ok(service.buscarPorProfesional(rutProfesional));
    }

    @GetMapping("/cliente/{rutCliente}")
    @Operation(summary = "Obtener citas por cliente", description = "Recupera una lista de citas para un cliente específico")
    @ApiResponse(responseCode = "200", description = "Lista de citas obtenida correctamente")
    @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    public ResponseEntity<List<AgendaResponseDTO>> porCliente(@PathVariable ("rutCliente") String rutCliente) {
        return ResponseEntity.ok(service.buscarPorCliente(rutCliente));
    }

    @GetMapping("/fecha/{fecha}")
    @Operation(summary = "Obtener citas por fecha", description = "Recupera una lista de citas para una fecha específica")
    @ApiResponse(responseCode = "200", description = "Lista de citas obtenida correctamente")
    @ApiResponse(responseCode = "404", description = "No se encontraron citas para la fecha especificada")
    public ResponseEntity<List<AgendaResponseDTO>> porFecha(@PathVariable ("fecha") String fecha) {
        return ResponseEntity.ok(service.buscarPorFecha(fecha));
    }

    @PostMapping
    @Operation(summary = "Crear cita", description = "Crea una nueva cita en la agenda")
    @ApiResponse(responseCode = "201", description = "Cita creada correctamente")
    public ResponseEntity<AgendaResponseDTO> crear(@Valid @RequestBody AgendaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cita", description = "Actualiza una cita existente en la agenda")
    @ApiResponse(responseCode = "200", description = "Cita actualizada correctamente")
    @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    public ResponseEntity<AgendaResponseDTO> actualizar(
            @PathVariable ("id") Integer idAgenda,
            @Valid @RequestBody AgendaRequestDTO dto) {
        return service.actualizar(idAgenda, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cita", description = "Elimina una cita existente en la agenda")
    @ApiResponse(responseCode = "204", description = "Cita eliminada correctamente")
    @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    public ResponseEntity<Void> eliminar(@PathVariable ("id") Integer idAgenda) {
        if (service.obtenerPorId(idAgenda).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.eliminar(idAgenda);
        return ResponseEntity.noContent().build();
    }
}
