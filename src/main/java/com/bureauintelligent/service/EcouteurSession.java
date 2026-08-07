package com.bureauintelligent.service;

import com.bureauintelligent.model.SessionTravail;

/**
 * Point d'accroche pour réagir aux transitions d'une session de travail.
 *
 * <p>Aucune notification (son, popup, LED) n'est implémentée ici : cette
 * interface sera branchée par {@code feature/notifications}. Pour l'instant,
 * une implémentation console est utilisée dans la démo et les tests.</p>
 */
public interface EcouteurSession {

    default void surDemarrage(SessionTravail session) {
    }

    /** La phase de travail vient de se terminer : une pause commence. */
    default void surFinDeTravail(SessionTravail session) {
    }

    /** La pause vient de se terminer : le travail reprend. */
    default void surFinDePause(SessionTravail session) {
    }

    default void surArret(SessionTravail session) {
    }
}
