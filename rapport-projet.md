# Rapport Projet - Concert Ticketing API

## 1. Contexte et objectifs

Ce projet a pour objectif de développer une API REST de billetterie de concerts, avec une séparation claire des responsabilités entre:

- visiteur / client (consultation, achat),
- organisateur (gestion de ses concerts, artistes, billets, statistiques),
- administrateur (gestion globale).

Les objectifs fonctionnels principaux étaient:

- exposer une API proprement documentée via OpenAPI/Swagger,
- sécuriser les endpoints par JWT et rôles,
- mettre en place des DTO d'entrée/sortie pour stabiliser le contrat API,
- fournir une base solide pour un back-office organisateur.

## 2. Périmêtre fonctionnel réalisé

## 2.1 Authentification et identité

- `POST /api/auth/login`
- `POST /api/auth/register`
- `POST /api/auth/logout`
- `GET /api/auth/me`
- `GET /api/auth/me/role`

Le JWT contient l'identité (email) et les rôles. Le filtre d'authentification protège les routes sensibles via `@RolesAllowed`.

## 2.2 Espace client

- `GET /api/latestConcerts`
- `GET /api/incomingConcerts`
- `GET /api/search`
- `GET /api/{id}`
- `GET /api/custom/profile`
- `GET /api/custom/mytickets`
- `POST /api/custom/buyticket`

## 2.3 Back-office organisateur

- Dashboard global: `GET /organise/dashboard`
- Concerts: `GET/POST/PUT/DELETE /organise/concerts...`
- Tickets: `GET/POST/PUT/DELETE /organise/tickets...`
- Artistes: `GET/POST/PUT/DELETE /organise/artists...`
- Clients d'un concert: `GET /organise/concerts/{id}/customers`
- Statistiques organisateur: `GET /organise/concerts/stats/dashboard`

La logique de possession est appliquée: un organisateur ne manipule que ses propres ressources.

## 2.4 Espace gestion / administration

- `/manage/admin`
- `/organise/organizers`

## 3. Architecture technique

Le projet suit une architecture en couches:

- `rest/`: ressources HTTP (contrats API)
- `service/`: logique applicative
- `dao/`: accès aux données (JPA)
- `domain/`: entités persistées
- `dto/`: objets de transfert + mapping
- `filters/`: sécurité transversale (JWT)

Cette organisation améliore la maintenabilité et limite le couplage entre HTTP, logique métier et persistance.

## 4. Choix techniques

- Framework REST: Resteasy (JAX-RS Jakarta)
- Persistance: Hibernate/JPA
- Base locale: HSQLDB (mode serveur)
- Documentation API: Swagger/OpenAPI v3
- Sécurité: JWT (jjwt) + BCrypt
- Build: Maven
- Exécution locale: Jetty Maven Plugin

## 5. Sécurité

## 5.1 Auth JWT

- Génération de token à la connexion.
- Validation du token dans `JWTAuthFilter`.
- Injection du principal dans `SecurityContext`.

## 5.2 Rôles

Le système utilise les rôles:

- `ADMIN`
- `ORGANIZER`
- `CUSTOMER`

Le rôle métier découle du type utilisateur (polymorphisme, colonne discriminante `dtype`) et est embarqué dans le JWT.

## 5.3 Contrôle d'accès

- contrôle statique via annotations `@RolesAllowed`,
- contrôle métier (ownership) dans les ressources organisateur.

## 6. Documentation API

Swagger est intégré et accessible via:

- `GET /openapi.json`
- `GET /api/docs`

Les ressources ont été annotées (`@Tag`, `@Operation`, `@ApiResponse`) pour un affichage organisé.

## 7. Gestion des données et initialisation

Les données seed sont injectées via `DataInitializer` pour accélérer les tests.

Points clés:

- script one-shot de démarrage (`run-all.bat`, `run-all.sh`),
- comptes de démo admin/organizer/customer,
- mots de passe hashés (BCrypt).

## 8. Difficultés rencontrées

- cohérence des mappings et chemins Swagger UI,
- stabilisation des DTO (éviter d'exposer les entités JPA),
- protection fine des endpoints selon le rôle,
- hétérogénéité de certains chemins historiques (`/api`, `/organise`, `/manage`).

## 9. Limites actuelles

- absence de suite de tests automatisés complète (unitaires + intégration),
- conventions de routes encore partiellement hétérogènes,
- certaines validations métiers peuvent être renforcées (capacité ticket, cohérence dates, etc.),
- pas encore de mécanisme de révocation JWT serveur (token stateless).

## 10. Améliorations recommandées

## 10.1 Court terme

- homogénéiser les préfixes d'URL (`/api/...`),
- ajouter un format d'erreur unifié (`ErrorResponseDto`),
- complèter les validations DTO (`@NotNull`, `@Email`, etc.).

## 10.2 Moyen terme

- mettre en place tests unitaires/services et tests API,
- introduire pagination/tri pour les listes,
- fiabiliser la logique ownership via un middleware/service dédié.

## 10.3 Long terme

- migration vers une base SQL de production (MySQL/PostgreSQL) avec migrations versionnées,
- observabilité (logs structurés, métriques),
- durcissement sécurité (refresh tokens, rotation secrets, blacklist éventuelle).

## 11. Bilan

Le projet fournit une base API complète, documentée et sécurisée, avec une vraie séparation des rôles et un back-office organisateur exploitable.

Le socle est prêt pour:

- l'intégration front complête,
- le renforcement des règles métier,
- l'industrialisation (tests, CI/CD, observabilité).
