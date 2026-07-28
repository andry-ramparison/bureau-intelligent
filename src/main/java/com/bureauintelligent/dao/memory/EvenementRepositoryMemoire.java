package com.bureauintelligent.dao.memory;

import com.bureauintelligent.dao.EvenementRepository;
import com.bureauintelligent.exception.EntiteIntrouvableException;
import com.bureauintelligent.model.EvenementCalendrier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Implémentation en mémoire de {@link EvenementRepository}.
 * Sera remplacée par une implémentation SQLite dans {@code feature/database}.
 */
public class EvenementRepositoryMemoire implements EvenementRepository {

    private final Map<Long, EvenementCalendrier> evenements = new ConcurrentHashMap<>();
    private final AtomicLong prochainId = new AtomicLong(1);

    @Override
    public EvenementCalendrier creer(EvenementCalendrier evenement) {
        long id = prochainId.getAndIncrement();
        evenement.setId(id);
        evenements.put(id, evenement);
        return evenement;
    }

    @Override
    public Optional<EvenementCalendrier> trouverParId(Long id) {
        return Optional.ofNullable(evenements.get(id));
    }

    @Override
    public List<EvenementCalendrier> trouverTout() {
        return List.copyOf(evenements.values());
    }

    @Override
    public EvenementCalendrier mettreAJour(EvenementCalendrier evenement) {
        exigerExistant(evenement.getId());
        evenements.put(evenement.getId(), evenement);
        return evenement;
    }

    @Override
    public void supprimer(Long id) {
        exigerExistant(id);
        evenements.remove(id);
    }

    @Override
    public List<EvenementCalendrier> trouverEntre(LocalDateTime debut, LocalDateTime fin) {
        return evenements.values().stream()
                .filter(e -> e.chevauche(debut, fin))
                .collect(Collectors.toList());
    }

    private void exigerExistant(Long id) {
        if (id == null || !evenements.containsKey(id)) {
            throw new EntiteIntrouvableException("Aucun événement avec l'id " + id);
        }
    }
}
