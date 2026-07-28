package com.bureauintelligent.model;

/**
 * Niveau de priorité d'une tâche.
 * L'ordre naturel de l'enum (BASSE < MOYENNE < HAUTE) est utilisé
 * pour le tri : {@link #ordinal()} croissant = priorité croissante.
 */
public enum Priorite {
    BASSE,
    MOYENNE,
    HAUTE
}
