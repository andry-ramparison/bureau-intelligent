# Conventions du projet — Bureau Intelligent

## Convention de branches (Git Flow adapté)

- `main` : toujours stable, ne reçoit que des merges validés.
- `feature/<nom>` : développement d'une fonctionnalité.
- `chore/<nom>` : tâches techniques (setup, configuration, outillage).
- `design/<nom>` : conception (ex. modèles OpenSCAD).
- `docs/<nom>` : documentation.
- `refactor/<nom>` : nettoyage, optimisation, sans nouvelle fonctionnalité.

Chaque branche doit être fusionnée dans `main` via une Pull Request une fois
son contenu validé (build + tests verts).

## Convention de commits

Format recommandé (inspiré de Conventional Commits) :

```
<type>(<portée>): <résumé court à l'impératif>
```

Types utilisés : `feat`, `fix`, `chore`, `docs`, `refactor`, `test`, `style`.

Exemples :
- `feat(work-session): ajouter le minuteur pomodoro`
- `chore(setup): configurer Maven et JavaFX`
- `docs: ajouter le manuel utilisateur`

## Convention de code Java

- Package racine : `com.bureauintelligent`.
- Sous-packages par responsabilité : `model`, `dao`, `service`, `ui`,
  `simulation`, `hardware`, `util`.
- Nommage : `PascalCase` pour les classes, `camelCase` pour les méthodes
  et variables, `UPPER_SNAKE_CASE` pour les constantes.
- Une classe publique par fichier.
- Javadoc sur les classes publiques et les méthodes non triviales.
- Pas de logique métier dans les classes d'interface (`ui`) : elles
  appellent des services (`service`).
- Les accès aux données passent exclusivement par la couche `dao`.

## Style

- Indentation : 4 espaces, pas de tabulations.
- Longueur de ligne indicative : 120 caractères.
- Fichiers encodés en UTF-8, fins de ligne LF.
