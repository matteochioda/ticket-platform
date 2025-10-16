// può modificare i propri dati dalla sua pagina tra cui lo stato personale in “non attivo” solo se non ha nemmeno un ticket in stato “da fare” o “in corso”


package org.milestone4.ticket_platform.controller;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

import org.milestone4.ticket_platform.model.Categoria;
import org.milestone4.ticket_platform.model.Nota;
import org.milestone4.ticket_platform.model.Ticket;
import org.milestone4.ticket_platform.model.User;
import org.milestone4.ticket_platform.repository.CategoriaRepository;
import org.milestone4.ticket_platform.repository.NotaRepository;
import org.milestone4.ticket_platform.repository.TicketRepository;
import org.milestone4.ticket_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;



@Controller
@RequestMapping("/giullstravells")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private NotaRepository notaRepository;



    // GetMapping - index
    @GetMapping("/tickets")
    public String index(@RequestParam(name="filtro", required=false, defaultValue= "TUTTI") String filtro, @RequestParam(name="keyword", required=false) String keyword, Model model) {
        List<Ticket> listaTicket = null;
        if (filtro.equals("DA_GESTIRE")) {
            listaTicket = ticketRepository.findByStato(filtro);
        } else if (filtro.equals("IN_CORSO")) {
            listaTicket = ticketRepository.findByStato(filtro);
        } else if (filtro.equals("COMPLETATO")) {
            listaTicket = ticketRepository.findByStato(filtro);
        } else if (keyword == null || keyword.isBlank()) {
            listaTicket = ticketRepository.findAll();
        } else {
            listaTicket = ticketRepository.findByTitoloContainingIgnoreCase(keyword);
        }
        model.addAttribute("listaTicket", listaTicket);
        model.addAttribute("filtroSelezionato", filtro);
        return "tickets/index";
    }



    // GetMapping - show
    @GetMapping("/tickets/show/{id}")
    public String show(@PathVariable("id") Integer id, Principal principal, Model model) {
        Ticket ticket = ticketRepository.findById(id).get();
        model.addAttribute("ticket", ticket);
        List<Nota> listaNote = notaRepository.findByTicket(ticket);
        model.addAttribute("listaNote", listaNote);
        User user = userRepository.findByUsername(principal.getName()).get();
        model.addAttribute("user", user);
        String utenteLoggato = principal.getName();
        model.addAttribute("utenteLoggato", utenteLoggato);
        boolean stessoUtente = ticket.getAssegnatoA() != null && ticket.getAssegnatoA().getUsername().equals(principal.getName());
        model.addAttribute("stessoUtente", stessoUtente);
        return "tickets/show";
    }

    // PostMapping - show - modifica stato ticket
    @PostMapping("/tickets/show/{id}")
    public String updateStato(@PathVariable("id") Integer id, @RequestParam("stato") String nuovoStato) {
    Ticket ticket = ticketRepository.findById(id).get();
    ticket.setStato(nuovoStato);
    ticketRepository.save(ticket);
    return "redirect:/giullstravells/tickets/show/" + id;
    }

    // PostMapping - show - creazione nota
    @PostMapping("/tickets/show/{id}/note")
    public String addNota(@PathVariable("id") Integer id, @RequestParam("testo") String testo, Principal principal) {
    Ticket ticket = ticketRepository.findById(id).get();
    User user = userRepository.findByUsername(principal.getName()).get();
    Nota nota = new Nota();
    nota.setTesto(testo);
    nota.setTicket(ticket);
    nota.setUser(user);
    notaRepository.save(nota);
    return "redirect:/giullstravells/tickets/show/" + id;
    }

    // PostMapping - show - nota
    @PostMapping("/tickets/show/{ticketId}/note/{notaId}")
    public String deleteNota(@PathVariable("ticketId") Integer ticketId, @PathVariable("notaId") Integer notaId) {
        notaRepository.deleteById(notaId);
        return "redirect:/giullstravells/tickets/show/" + ticketId;
    }



    // GetMapping - create
    @GetMapping("/tickets/create")
    public String create(Model model) {
        model.addAttribute("ticket", new Ticket());
        List<User> listaOperatori = userRepository.findByRuoliNomeAndStato("OPERATORE", "attivo");
        listaOperatori.sort(Comparator.comparing(User::getCognome).thenComparing(User::getNome));
        model.addAttribute("listaOperatori", listaOperatori);
        List<Categoria> listaCategorie = categoriaRepository.findAll();
        model.addAttribute("listaCategorie", listaCategorie);
        return "tickets/create";
    }

    // PostMapping - create
    @PostMapping("/tickets/create")
    public String save(@Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult, RedirectAttributes redirectAttributes, Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("listaOperatori", userRepository.findByRuoliNomeAndStato("OPERATORE", "attivo"));
            model.addAttribute("listaCategorie", categoriaRepository.findAll());
            return "tickets/create";
        }
        User creatore = userRepository.findByUsername(principal.getName()).get();
        formTicket.setCreatoDa(creatore);
        ticketRepository.save(formTicket);
        redirectAttributes.addFlashAttribute("successMessage", "Il Ticket è stato creato con successo!");
        return "redirect:/giullstravells/tickets";
    }

    

    // GetMapping - edit
    @GetMapping("/tickets/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("ticket", ticketRepository.findById(id).get());
        List<User> listaOperatori = userRepository.findByRuoliNomeAndStato("OPERATORE", "attivo");
        listaOperatori.sort(Comparator.comparing(User::getCognome).thenComparing(User::getNome)); 
        model.addAttribute("listaOperatori", listaOperatori);
        List<Categoria> listaCategorie = categoriaRepository.findAll();
        model.addAttribute("listaCategorie", listaCategorie);
        return "tickets/edit";
    }

    // PostMapping - edit
    @PostMapping("/tickets/edit/{id}")
    public String update(@Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult, RedirectAttributes redirectAttributes,Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("listaOperatori", userRepository.findByRuoliNomeAndStato("OPERATORE", "attivo"));
            model.addAttribute("listaCategorie", categoriaRepository.findAll());
            return "tickets/edit";
        }
        User creatore = userRepository.findByUsername(principal.getName()).get();
        formTicket.setCreatoDa(creatore);
        ticketRepository.save(formTicket);
        redirectAttributes.addFlashAttribute("successEditMessage", "Il Ticket è stato aggiornato con successo!");
        return "redirect:/giullstravells/tickets";
    }



    // PostMapping - delete - ticket
    @PostMapping("/tickets/delete/{id}")
    public String deleteTicket(@PathVariable("id") Integer id) {
        ticketRepository.deleteById(id);
        return "redirect:/giullstravells/tickets";
    }

}
