package com.bureauintelligent.service;

import com.bureauintelligent.model.Notification;

/**
 * Un canal par lequel une {@link Notification} peut être diffusée.
 *
 * <p>Implémentation actuelle : console (voir {@code service.canal.CanalConsole}),
 * utilisée pour les tests et la démo. D'autres canaux viendront se greffer
 * sans changer {@link GestionnaireNotifications} :</p>
 * <ul>
 *   <li>popup visuel dans l'UI JavaFX — {@code feature/dashboard}</li>
 *   <li>buzzer / LEDs sur le boîtier Arduino — {@code feature/hardware-integration}</li>
 * </ul>
 */
public interface CanalNotification {

    void diffuser(Notification notification);
}
