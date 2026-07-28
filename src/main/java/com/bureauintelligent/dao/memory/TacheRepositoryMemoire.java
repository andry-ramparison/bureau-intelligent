package com.bureauintelligent.dao.memory;

import com.bureauintelligent.dao.TacheRepository;
import com.bureauintelligent.exception.EntiteIntrouvableException;
import com.bureauintelligent.model.StatutTache;
import com.bureauintelligent.model.Tache;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Implémentation en mémoire de {@link TacheRepository}.
 *
 * <p>Destinée à être remplacée par une implémentation SQLite dans
 * {@code feature/database}, sans changer le contrat {@link TacheRepository}.</p>
 */
public class TacheRepositoryMemoire implements TacheRepository {

    private final Map<Long, Tache> taches = new ConcurrentHashMap<>();
    private final AtomicLong prochainId = new AtomicLong(1);

    @Override
    public Tache creer(Tache tache) {
        long id = prochainId.getAndIncrement();
        tache.setId(id);
        taches.put(id, tache);
        return tache;
    }

    @Override
    public Optional<Tache> trouverParId(Long id) {
        return Optional.ofNullable(taches.get(id));
    }

    @Override
    public List<Tache> trouverTout() {
        return List.copyOf(taches.values());
    }

    @Override
    public Tache mettreAJour(Tache tache) {
        exigerExistante(tache.getId());
        taches.put(tache.getId(), tache);
        return tache;
    }

    @Override
    public void supprimer(Long id) {
        exigerExistante(id);
        taches.remove(id);
    }

    @Override
    public List<Tache> trouverParStatut(StatutTache statut) {
        return taches.values().stream()
                .filter(t -> t.getStatut() == statut)
                .collect(Collectors.toList());
    }

    private void exigerExistante(Long id) {
        if (id == null || !taches.containsKey(id)) {
            throw new EntiteIntrouvableException("Aucune tâche avec l'id " + id);
        }
    }
}
