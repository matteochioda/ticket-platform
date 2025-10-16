package org.milestone4.ticket_platform.controller;

import java.security.Principal;
import java.util.List;

import org.milestone4.ticket_platform.model.Ticket;
import org.milestone4.ticket_platform.model.User;
import org.milestone4.ticket_platform.repository.TicketRepository;
import org.milestone4.ticket_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TicketRepository ticketRepository;

    // GetMapping - show
    @GetMapping("/{username}")
    public String show(@PathVariable("username") String username, Principal principal, Model model) {
        User user = userRepository.findByUsername(username).get();
        model.addAttribute("user", user);
        String utenteLoggato = principal.getName();
        model.addAttribute("utenteLoggato", utenteLoggato);
        List<Ticket> listaTicketAssegnati;
        listaTicketAssegnati = user.getTicketsAssegnati();
        model.addAttribute("listaTicketAssegnati", listaTicketAssegnati);
        List<Ticket> ticketNonCompletati = ticketRepository.findByAssegnatoAAndStatoNot(user, "COMPLETATO");
        model.addAttribute("ticketNonCompletati", ticketNonCompletati);
        boolean stessoUtente = user.getUsername().equals(principal.getName());
        model.addAttribute("stessoUtente", stessoUtente);
        return "/users/show";
    }
    
    // PostMapping - edit
    @PostMapping("/{id}/edit")
    public String edit(@PathVariable("id") Integer id, @RequestParam("nome") String nome, @RequestParam("cognome") String cognome, @RequestParam("username") String username, Model model) {
        User user = userRepository.findById(id).get();
        String oldUsername = user.getUsername();
        user.setNome(nome);
        user.setCognome(cognome);
        user.setUsername(username);
        userRepository.save(user);
        if (!oldUsername.equals(user.getUsername())) {
            return "redirect:/login";
        }
        return "redirect:/users/" + username;
    }
    
    @PostMapping("/{username}/toggle")
    public String toggleStato(@PathVariable ("username") String username) {
    User user = userRepository.findByUsername(username).get();
    if (user != null) {
        user.setStato(user.getStato().equals("attivo") ? "non_attivo" : "attivo");
        userRepository.save(user);
    }
    return "redirect:/users/{username}";
}

}
