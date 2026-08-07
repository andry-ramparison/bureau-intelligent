package com.bureauintelligent.service.canal;

import com.bureauintelligent.model.Notification;
import com.bureauintelligent.service.CanalNotification;

/**
 * Canal de notification le plus simple : affiche la notification dans la
 * console. Utilisé pour les tests et la démo, en attendant les canaux
 * visuels (UI) et matériels (buzzer/LEDs) des branches suivantes.
 */
public class CanalConsole implements CanalNotification {

    @Override
    public void diffuser(Notification notification) {
        System.out.println(notification);
    }
}
