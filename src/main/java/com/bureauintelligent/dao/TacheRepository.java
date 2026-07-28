package com.bureauintelligent.dao;

import com.bureauintelligent.model.StatutTache;
import com.bureauintelligent.model.Tache;

import java.util.List;

/**
 * Repository dédié aux {@link Tache}.
 */
public interface TacheRepository extends Repository<Tache, Long> {

    List<Tache> trouverParStatut(StatutTache statut);
}
