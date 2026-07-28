package com.bureauintelligent.dao;

import com.bureauintelligent.model.EvenementCalendrier;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository dédié aux {@link EvenementCalendrier}.
 */
public interface EvenementRepository extends Repository<EvenementCalendrier, Long> {

    /** Retourne les événements chevauchant la fenêtre [debut, fin]. */
    List<EvenementCalendrier> trouverEntre(LocalDateTime debut, LocalDateTime fin);
}
