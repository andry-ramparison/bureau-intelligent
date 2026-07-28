package com.bureauintelligent.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Représente une tâche à réaliser dans l'espace de travail.
 *
 * <p>L'identifiant ({@code id}) est {@code null} tant que la tâche n'a pas
 * été persistée par un {@code TacheRepository} : c'est le repository qui
 * attribue l'identifiant définitif lors de la création.</p>
 */
public class Tache {

    private Long id;
    private String titre;
    private String description;
    private LocalDate dateEcheance;
    private Priorite priorite;
    private StatutTache statut;
    private final LocalDateTime dateCreation;

    public Tache(String titre, String description, LocalDate dateEcheance, Priorite priorite) {
        this.titre = exigerTitreValide(titre);
        this.description = description == null ? "" : description;
        this.dateEcheance = dateEcheance;
        this.priorite = priorite == null ? Priorite.MOYENNE : priorite;
        this.statut = StatutTache.A_FAIRE;
        this.dateCreation = LocalDateTime.now();
    }

    private static String exigerTitreValide(String titre) {
        if (titre == null || titre.isBlank()) {
            throw new IllegalArgumentException("Le titre de la tâche ne peut pas être vide.");
        }
        return titre;
    }

    public Long getId() {
        return id;
    }

    /** Réservé au repository : attribue l'identifiant lors de la première sauvegarde. */
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

    public LocalDate getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public Priorite getPriorite() {
        return priorite;
    }

    public void setPriorite(Priorite priorite) {
        this.priorite = priorite == null ? Priorite.MOYENNE : priorite;
    }

    public StatutTache getStatut() {
        return statut;
    }

    public void setStatut(StatutTache statut) {
        this.statut = statut == null ? StatutTache.A_FAIRE : statut;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    /** Vrai si la date d'échéance est dépassée et que la tâche n'est pas terminée. */
    public boolean estEnRetard(LocalDate aujourdHui) {
        return statut != StatutTache.TERMINEE
                && dateEcheance != null
                && dateEcheance.isBefore(aujourdHui);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tache)) return false;
        Tache tache = (Tache) o;
        return Objects.equals(id, tache.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Tache{id=%s, titre='%s', priorite=%s, statut=%s, echeance=%s}"
                .formatted(id, titre, priorite, statut, dateEcheance);
    }
}
