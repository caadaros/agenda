package com.peluqueria.agenda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendaResponseDTO {

    private Integer idAgenda;
    private String fecha;
    private String horaInicio;
    private Long idTipoServicio;
    private String rutProfesional;
    private String rutCliente;
    private String estadoCita;
}

