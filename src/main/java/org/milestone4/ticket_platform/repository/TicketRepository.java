package org.milestone4.ticket_platform.repository;

import java.util.List;

import org.milestone4.ticket_platform.model.Ticket;
import org.milestone4.ticket_platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TicketRepository extends JpaRepository<Ticket, Integer>{

    public List<Ticket> findByStato(String stato);

    public List<Ticket> findByTitoloContainingIgnoreCase(String titolo);

    public List<Ticket> findByAssegnatoAAndStatoNot(User assegnatoA, String stato);

    public List<Ticket> findByCategoriaNome(String nome);

}
