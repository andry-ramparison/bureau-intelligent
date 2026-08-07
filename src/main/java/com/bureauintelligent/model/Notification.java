package com.bureauintelligent.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Représente une notification à diffuser à l'utilisateur (rappel, fin de
 * session, début/fin de pause, alerte visuelle, ...).
 *
 * <p>Cette classe ne sait pas *comment* s'afficher (LCD, popup, buzzer,
 * LED) : c'est le rôle des {@code CanalNotification} qui la reçoivent.</p>
 */
public class Notification {

    private final TypeNotification type;
    private final String titre;
    private final String message;
    private final NiveauAlerte niveau;
    private final LocalDateTime horodatage;

    public Notification(TypeNotification type, String titre, String message, NiveauAlerte niveau) {
        this.type = Objects.requireNonNull(type, "Le type de notification est obligatoire.");
        this.titre = exigerTitreValide(titre);
        this.message = message == null ? "" : message;
        this.niveau = niveau == null ? NiveauAlerte.INFO : niveau;
        this.horodatage = LocalDateTime.now();
    }

    private static String exigerTitreValide(String titre) {
        if (titre == null || titre.isBlank()) {
            throw new IllegalArgumentException("Le titre de la notification ne peut pas être vide.");
        }
        return titre;
    }

    public TypeNotification getType() {
        return type;
    }

    public String getTitre() {
        return titre;
    }

    public String getMessage() {
        return message;
    }

    public NiveauAlerte getNiveau() {
        return niveau;
    }

    public LocalDateTime getHorodatage() {
        return horodatage;
    }

    @Override
    public String toString() {
        return "[%s][%s] %s — %s".formatted(niveau, type, titre, message);
    }
}
