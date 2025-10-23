package org.milestone4.ticket_platform.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="users")
public class User {

    // ATTRIBUTI

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable=false)
    private Integer id;

    @NotNull
    @NotBlank(message = "Nome is mandatory")
    @Size(min = 3, max = 30, message = "Il nome deve contenere tra 3 e 30 caratteri")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$", message = "Il nome può contenere solo lettere")
    @Column(nullable=false)
    private String nome;

    @NotNull
    @NotBlank(message = "Cognome is mandatory")
    @Size(min = 3, max = 30, message = "Il cognome deve contenere tra 3 e 30 caratteri")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$", message = "Il cognome può contenere solo lettere")
    @Column(nullable=false)
    private String cognome;

    @NotNull
    @NotBlank(message = "Email is mandatory")
    @Size(min = 3, max = 20, message = "Lo username deve contenere tra 3 e 20 caratteri")
    @Pattern(regexp = "^[A-Za-z0-9_.-]+$", message = "Lo username può contenere solo lettere, numeri, punti e trattini")
    @Column(nullable=false, unique=true)
    private String username;

    @NotNull
    @Size(min = 8, message = "La password deve contenere almeno 8 caratteri")
    @Pattern(regexp = "^(\\{noop\\})?(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
    message = "La password deve contenere almeno una lettera maiuscola, una minuscola, un numero e un carattere speciale")
    @Column(nullable=false)
    private String password;

    @CreationTimestamp
    @Column(nullable=false, updatable=false)
    private LocalDateTime registrationDate;

    @NotNull
    private String stato = "non_attivo";
    
    @OneToMany(mappedBy="user", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Nota> note = new ArrayList<>();

    @OneToMany(mappedBy="creatoDa")
    private List<Ticket> ticketsCreati = new ArrayList<>();

    @OneToMany(mappedBy="assegnatoA")
    private List<Ticket> ticketsAssegnati = new ArrayList<>();

    @ManyToMany(fetch=FetchType.EAGER)
    private List<Ruolo> ruoli;



    // GETTERS & SETTERS

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String email) {
        this.username = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public List<Nota> getNote() {
        return note;
    }

    public void setNote(List<Nota> note) {
        this.note = note;
    }

    public List<Ticket> getTicketsCreati() {
        return ticketsCreati;
    }

    public void setTicketsCreati(List<Ticket> ticketsCreati) {
        this.ticketsCreati = ticketsCreati;
    }

    public List<Ticket> getTicketsAssegnati() {
        return ticketsAssegnati;
    }

    public void setTicketsAssegnati(List<Ticket> ticketsAssegnati) {
        this.ticketsAssegnati = ticketsAssegnati;
    }

    public List<Ruolo> getRuoli() {
        return ruoli;
    }

    public void setRuoli(List<Ruolo> ruoli) {
        this.ruoli = ruoli;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    

    // METODI
    public String getFormattedRegistrationDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy - HH:mm");
        return registrationDate.format(formatter);
    }

}
