package com.peluqueria.agenda.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para la solicitud de creación o actualización de una cita")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendaRequestDTO {

    @Schema(description = "Fecha de la cita", example = "2023-10-10")
    @NotBlank(message = "La fecha es obligatoria")
    private String fecha;

    @Schema(description = "Hora de inicio de la cita", example = "09:00")
    @NotBlank(message = "La hora es obligatoria")
    private String horaInicio;

    @Schema(description = "ID del tipo de servicio", example = "1")
    @NotNull(message = "El servicio es obligatorio")
    private Long idTipoServicio;

    @Schema(description = "RUT del profesional", example = "12345678-9")
    @NotBlank(message = "El profesional es obligatorio")
    private String rutProfesional;

    @Schema(description = "RUT del cliente", example = "987654321")
    @NotBlank(message = "El cliente es obligatorio")
    private String rutCliente;

    @Schema(description = "Estado de la cita", example = "pendiente")
    @NotBlank(message = "El estado es obligatorio")
    private String estadoCita;
}