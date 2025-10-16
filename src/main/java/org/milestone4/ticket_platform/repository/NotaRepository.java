package org.milestone4.ticket_platform.repository;

import java.util.List;

import org.milestone4.ticket_platform.model.Nota;
import org.milestone4.ticket_platform.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaRepository extends JpaRepository<Nota, Integer>{

    List<Nota> findByTicket(Ticket ticket);

}
