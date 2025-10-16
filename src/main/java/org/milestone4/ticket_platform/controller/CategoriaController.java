package org.milestone4.ticket_platform.controller;

import java.util.List;

import org.milestone4.ticket_platform.model.Categoria;
import org.milestone4.ticket_platform.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/giullstravells")
public class CategoriaController {
    
    @Autowired
    private CategoriaRepository categoriaRepository;



    // GetMapping - index
    @GetMapping("/categorie")
    public String index(Model model) {
        List<Categoria> listaCategorie = categoriaRepository.findAll();
        model.addAttribute("listaCategorie", listaCategorie);
        return "categorie/index";
    }



    // GetMapping - show
    @GetMapping("/categorie/show/{id}")
    public String show(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("categoria", categoriaRepository.findById(id).get());
        return "categorie/show";
    }
    


    // GetMapping - create
    @GetMapping("/categorie/create")
    public String create(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categorie/create";
    }

    // PostMapping - create
    @PostMapping("/categorie/create")
    public String save(@Valid @ModelAttribute("categoria") Categoria formCategoria, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            return "categorie/create";
        }
        categoriaRepository.save(formCategoria);
        redirectAttributes.addFlashAttribute("successMessage", "La Categoria è stata creata con successo!");
        return "redirect:/giullstravells/categorie";
    }



    // GetMapping - edit
    @GetMapping("/categorie/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("categoria", categoriaRepository.findById(id).get());
        return "categorie/edit";
    }

    // PostMapping - edit
    @PostMapping("/categorie/edit/{id}")
    public String update(@Valid @ModelAttribute("categorie") Categoria formCategoria, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            return "categorie/edit/{id}";
        }
        categoriaRepository.save(formCategoria);
        redirectAttributes.addFlashAttribute("successEditMessage", "La categoria è stata aggiornata con successo!");
        return "redirect:/giullstravells/categorie";
    }



    // PostMapping - delete
    @PostMapping("/categorie/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        Categoria categoria = categoriaRepository.findById(id).get();
        if (categoria != null && !categoria.getTickets().isEmpty()) {
        redirectAttributes.addFlashAttribute("errorMessage",
            "Impossibile eliminare la categoria: ci sono ticket associati.");
        return "redirect:/giullstravells/categorie";
        }

        categoriaRepository.deleteById(id);
        return "redirect:/giullstravells/categorie";
    }

}
