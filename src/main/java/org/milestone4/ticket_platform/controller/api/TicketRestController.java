package org.milestone4.ticket_platform.controller.api;

import java.util.List;

import org.milestone4.ticket_platform.model.Ticket;
import org.milestone4.ticket_platform.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@CrossOrigin
@RequestMapping("/api/giullstravells/tickets")
public class TicketRestController {

    @Autowired
    TicketRepository ticketRepository;

    // Leggere tutti i ticket
    @GetMapping
    public List<Ticket> index(@RequestParam(name="filtro", required=false, defaultValue= "TUTTI") String filtro, @RequestParam(name="keyword", required=false) String keyword, Model model) {
        List<Ticket> result = null;
        if (filtro.equals("DA_GESTIRE")) {
            result = ticketRepository.findByStato(filtro);
        } else if (filtro.equals("IN_CORSO")) {
            result = ticketRepository.findByStato(filtro);
        } else if (filtro.equals("COMPLETATO")) {
            result = ticketRepository.findByStato(filtro);
        } else if (keyword == null || keyword.isBlank()) {
            result = ticketRepository.findAll();
        } else {
            result = ticketRepository.findByTitoloContainingIgnoreCase(keyword);
        }
        return result;
    }

    // Leggere ticket per ID
    @GetMapping("{id}")
    public Ticket getById(@PathVariable("id") Integer id) {
        return ticketRepository.findById(id).get();
    }

    // Leggere ticket per STATO
    @GetMapping("/stato/{stato}")
    public List<Ticket> getByStato(@PathVariable("stato") String stato) {
        return ticketRepository.findByStato(stato);
    }

    // Leggere ticket per CATEGORIA
    @GetMapping("/categoria/{categoriaNome}")
    public List<Ticket> getByCategoria(@PathVariable("categoriaNome") String categoriaNome) {
        return ticketRepository.findByCategoriaNome(categoriaNome);
    }

    // Creare nuovo ticket
    @PostMapping
    public Ticket create(@RequestBody Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    // Modifica ticket
    @PutMapping("{id}")
    public Ticket put(@PathVariable String id, @RequestBody Ticket entity) {
        return ticketRepository.save(entity);
    }

    // Elimina ticket
    @DeleteMapping("id")
    public void delete(@PathVariable("id") Integer id) {
        ticketRepository.deleteById(id);
    }

    
}
