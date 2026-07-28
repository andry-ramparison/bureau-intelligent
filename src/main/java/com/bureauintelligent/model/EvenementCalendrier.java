package com.bureauintelligent.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Représente un événement placé dans le calendrier
 * (peut être lié à une {@link Tache} ou totalement indépendant).
 */
public class EvenementCalendrier {

    private Long id;
    private String titre;
    private String description;
    private LocalDateTime debut;
    private LocalDateTime fin;
    private Long tacheAssocieeId;

    public EvenementCalendrier(String titre, String description, LocalDateTime debut, LocalDateTime fin) {
        this.titre = exigerTitreValide(titre);
        this.description = description == null ? "" : description;
        this.debut = Objects.requireNonNull(debut, "La date de début est obligatoire.");
        this.fin = Objects.requireNonNull(fin, "La date de fin est obligatoire.");
        if (fin.isBefore(debut)) {
            throw new IllegalArgumentException("La date de fin ne peut pas précéder la date de début.");
        }
    }

    private static String exigerTitreValide(String titre) {
        if (titre == null || titre.isBlank()) {
            throw new IllegalArgumentException("Le titre de l'événement ne peut pas être vide.");
        }
        return titre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = exigerTitreValide(titre);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? "" : description;
    }

    public LocalDateTime getDebut() {
        return debut;
    }

    public void setDebut(LocalDateTime debut) {
        this.debut = Objects.requireNonNull(debut);
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = Objects.requireNonNull(fin);
    }

    public Long getTacheAssocieeId() {
        return tacheAssocieeId;
    }

    public void setTacheAssocieeId(Long tacheAssocieeId) {
        this.tacheAssocieeId = tacheAssocieeId;
    }

    /** Vrai si l'événement chevauche l'intervalle [debutFenetre, finFenetre]. */
    public boolean chevauche(LocalDateTime debutFenetre, LocalDateTime finFenetre) {
        return !fin.isBefore(debutFenetre) && !debut.isAfter(finFenetre);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EvenementCalendrier)) return false;
        EvenementCalendrier that = (EvenementCalendrier) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "EvenementCalendrier{id=%s, titre='%s', debut=%s, fin=%s}"
                .formatted(id, titre, debut, fin);
    }
}
