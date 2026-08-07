package com.bureauintelligent.model;

/**
 * Catégorie d'une notification. {@code ALERTE_VISUELLE} est volontairement
 * générique : elle sera réutilisée par {@code feature/smart-monitoring}
 * pour les alertes de posture et d'absence, sans ajouter de nouveau type.
 */
public enum TypeNotification {
    RAPPEL_TACHE,
    FIN_SESSION_TRAVAIL,
    DEBUT_PAUSE,
    FIN_PAUSE,
    ALERTE_VISUELLE
}
