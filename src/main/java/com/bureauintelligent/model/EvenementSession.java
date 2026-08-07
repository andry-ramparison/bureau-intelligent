package com.bureauintelligent.model;

/**
 * Événement produit par {@link SessionTravail#avancer(long)} lorsqu'une
 * transition de phase a lieu. Consommé par le service appelant pour
 * déclencher les notifications appropriées (branche {@code feature/notifications}).
 */
public enum EvenementSession {
    /** Aucune transition : le décompte continue dans la même phase. */
    AUCUN,
    /** La phase de travail vient de se terminer (une pause commence). */
    FIN_TRAVAIL,
    /** Une pause (courte ou longue) vient de se terminer (le travail reprend). */
    FIN_PAUSE
}
