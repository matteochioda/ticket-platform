package org.milestone4.ticket_platform.controller;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

import org.milestone4.ticket_platform.model.Ruolo;
import org.milestone4.ticket_platform.model.Ticket;
import org.milestone4.ticket_platform.model.User;
import org.milestone4.ticket_platform.repository.RuoloRepository;
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
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired 
    RuoloRepository ruoloRepository;

    // GetMapping - index
    @GetMapping("")
    public String index(@RequestParam(name="keyword", required=false) String keyword ,Model model) {
        List<User> listaUtenti = null;
        if (keyword == null || keyword.isBlank()) {
            listaUtenti = userRepository.findAll();
            listaUtenti.sort(Comparator.comparing(User::getCognome).thenComparing(User::getNome));
        } else {
            listaUtenti = userRepository.findByNomeContainingIgnoreCaseOrCognomeContainingIgnoreCase(keyword, keyword);
            listaUtenti.sort(Comparator.comparing(User::getCognome).thenComparing(User::getNome));
        }
        model.addAttribute("listaUtenti", listaUtenti);
        return "/users/index";
    }


    
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


    
    // PostMapping - edit user details
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


    
    // PostMapping - stato utente
    @PostMapping("/{username}/toggle")
    public String toggleStato(@PathVariable ("username") String username) {
    User user = userRepository.findByUsername(username).get();
    if (user != null) {
        user.setStato(user.getStato().equals("attivo") ? "non_attivo" : "attivo");
        userRepository.save(user);
    }
    return "redirect:/users/{username}";
    }



    // GetMapping - create
    @GetMapping("/create")
    public String create(Model model) {
        List<Ruolo> ruoli = ruoloRepository.findAll();
        model.addAttribute("ruoli", ruoli);
        model.addAttribute("user", new User());
        return "/users/create";
    }

    // PostMapping - create
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("user") User formUser, BindingResult bindingResult, @RequestParam("ruoli") List<Integer> ruoloIds, RedirectAttributes redirectAttributes, Model model) {    
        if (bindingResult.hasErrors()) {
            List<Ruolo> ruoli = ruoloRepository.findAll();
            model.addAttribute("ruoli", ruoli);
            return "/users/create";
        }

        List<Ruolo> ruoliScelti = ruoloRepository.findAllById(ruoloIds);
        formUser.setRuoli(ruoliScelti);
        formUser.setStato("non_attivo");
        formUser.setPassword("{noop}" + formUser.getPassword());
        userRepository.save(formUser);
        redirectAttributes.addFlashAttribute("successMessage", "L'utente è stato creato con successo!");
        return "redirect:/users";
    }


    // GetMapping - edit
    @GetMapping("/edit/{username}")
    public String edit(@PathVariable("username") String username, Model model) {
        List<Ruolo> ruoli = ruoloRepository.findAll();
        model.addAttribute("ruoli", ruoli);
        User user = userRepository.findByUsername(username).get();
        model.addAttribute("user", user);
        return "/users/edit";
    }

    // PostMapping - update
    @PostMapping("/edit/{id}")
    public String update(@Valid @ModelAttribute("user") User formUser, BindingResult bindingResult, @RequestParam("ruoli") List<Integer> ruoloIds, RedirectAttributes redirectAttributes, Model model) {    
        if (bindingResult.hasErrors()) {
            List<Ruolo> ruoli = ruoloRepository.findAll();
            model.addAttribute("ruoli", ruoli);
            return "/users/edit";
        }
        List<Ruolo> ruoliScelti = ruoloRepository.findAllById(ruoloIds);
        formUser.setRuoli(ruoliScelti);
        formUser.setStato(formUser.getStato());
        formUser.setPassword(formUser.getPassword());
        userRepository.save(formUser);
        redirectAttributes.addFlashAttribute("successEditMessage", "L'utente è stato modificato con successo!");
        return "redirect:/users";
    }
    


    // PostMapping - delete
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        userRepository.deleteById(id);
        return "redirect:/users";
    }
    

}
