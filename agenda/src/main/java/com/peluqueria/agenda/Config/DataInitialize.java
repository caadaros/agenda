package com.peluqueria.agenda.Config;

import com.peluqueria.agenda.Model.Agenda;
import com.peluqueria.agenda.Repository.AgendaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitialize implements CommandLineRunner {

    private final AgendaRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            log.info(">>> Agenda: BD ya tiene datos, se omite la carga inicial.");
            return;
        }
        
        repository.save(new Agenda(null, "2026-05-25", "09:00", 2L, "222222221", "111111111", "Confirmado"));
        repository.save(new Agenda(null, "2026-05-26", "10:30", 1L, "222222221", "111111112", "Confirmado"));
        
        log.info(">>> Agenda: {} Citas insertadas.", repository.count());
    }
}
