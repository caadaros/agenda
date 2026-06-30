package com.peluqueria.agenda.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.peluqueria.agenda.Model.Agenda;

import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Integer> {

    List<Agenda> findByRutProfesional(String rutProfesional);

    List<Agenda> findByRutCliente(String rutCliente);

    List<Agenda> findByFecha(String fecha);

    boolean existsByRutProfesionalAndFechaAndHoraInicio(
            String rutProfesional,
            String fecha,
            String horaInicio);
}