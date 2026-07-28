package com.bureauintelligent.dao;

import java.util.List;
import java.util.Optional;

/**
 * Contrat CRUD générique implémenté par tous les repositories du projet.
 *
 * <p>Implémentation actuelle : en mémoire (voir {@code dao.memory}).
 * Une implémentation SQLite viendra la remplacer dans {@code feature/database}
 * sans que le code appelant (couche {@code service}) n'ait à changer.</p>
 */
public interface Repository<T, ID> {

    /** Crée l'entité et lui attribue un identifiant. Retourne l'entité créée. */
    T creer(T entite);

    Optional<T> trouverParId(ID id);

    List<T> trouverTout();

    /** Met à jour une entité existante. Lève une exception si l'id est inconnu. */
    T mettreAJour(T entite);

    /** Supprime l'entité. Lève une exception si l'id est inconnu. */
    void supprimer(ID id);
}
