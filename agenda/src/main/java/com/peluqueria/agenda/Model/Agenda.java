package com.peluqueria.agenda.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Agenda")
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAgenda;

    @Column(nullable = false)
    private String fecha;

    @Column(nullable = false)
    private String horaInicio;

    // Solo guardamos el id como referencia lógica.
    @Column(name="servicio", nullable=false, length = 20)
    private Long idTipoServicio;

    // Solo guardamos el rut como referencia lógica.
    @Column(name="rutProfesional", nullable=true, length = 10)
    private String rutProfesional;

    // Solo guardamos el rut como referencia lógica.
    @Column(name = "rutCliente", nullable = false, length = 10)
    private String rutCliente;

    @Column(name = "Estado", nullable = false,length = 20)
    private String estadoCita;
}