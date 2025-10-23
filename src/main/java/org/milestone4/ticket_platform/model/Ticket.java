package org.milestone4.ticket_platform.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="tickets")
public class Ticket {

    // ATTRIBUTI

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @NotBlank(message = "Titolo is mandatory")
    @Size(min = 3, max = 100, message = "Il titolo deve contenere tra 3 e 100 caratteri")
    @Column(nullable=false)
    private String titolo;

    @NotNull
    @NotBlank(message = "Descrizione is mandatory")
    @Column(nullable=false)
    private String descrizione;

    @NotNull(message = "Stato is mandatory")
    @Column(nullable=false)
    private String stato = "DA_GESTIRE";

    @NotNull
    @Column(nullable=false, updatable=false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy="ticket", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Nota> note = new ArrayList<>();
 
    @ManyToOne
    @NotNull(message="Categoria is mandatory")
    @JoinColumn(name="categoria_id")
    @JsonIgnoreProperties("tickets")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name="creato_da")
    @JsonBackReference(value="creatoDa")
    private User creatoDa;

    @ManyToOne
    @JoinColumn(name="assegnato_a")
    @JsonBackReference(value="assegnatoA")
    private User assegnatoA;

    

    // GETTERS & SETTERS

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Nota> getNote() {
        return note;
    }

    public void setNote(List<Nota> note) {
        this.note = note;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public User getCreatoDa() {
        return creatoDa;
    }

    public void setCreatoDa(User creatoDa) {
        this.creatoDa = creatoDa;
    }

    public User getAssegnatoA() {
        return assegnatoA;
    }

    public void setAssegnatoA(User assegnatoA) {
        this.assegnatoA = assegnatoA;
    }


    // METODI 
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
        return createdAt.format(formatter);
    }
    
}
