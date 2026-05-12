# Rapport projet - Etat actuel

## 1. Objectif
Le projet fournit une API REST de billetterie de concerts avec:
- authentification JWT,
- espace public de recherche,
- espace customer (achat, historique),
- back-office organizer (concerts, tickets, artistes, stats),
- notifications internes.

## 2. Stack technique
- Java 15
- Maven
- JAX-RS (Resteasy)
- Hibernate/JPA
- HSQLDB
- Swagger/OpenAPI v3
- JWT (jjwt)
- BCrypt

## 3. Fonctionnalites implementees

### 3.1 Auth
- `POST /api/auth/login`
- `POST /api/auth/register`
- `POST /api/auth/logout`
- `GET /api/auth/me`
- `GET /api/auth/me/role`

### 3.2 Home/Public
- concerts recents et a venir,
- recherche multi-criteres (topic, artiste, date, description, organisateur, min/max prix),
- detail concert,
- detail ticket.

### 3.3 Customer
- profil,
- mes tickets,
- mes achats,
- achat ticket (quantite),
- preferences notification,
- detail ticket.

### 3.4 Organizer
- CRUD concerts,
- CRUD tickets,
- CRUD artistes + liaison concert/artiste,
- clients par concert,
- dashboard et stats,
- ventes organizer (`sales/me`, `sales/me/history`).

### 3.5 Notifications
- notifications persistantes,
- lecture/unread,
- flux SSE temps reel,
- notifications metier sur ventes et nouveaux concerts.

## 4. Architecture
- `domain`: entites
- `dao`: acces donnees
- `service`: logique metier
- `dto`: contrats API
- `rest`: endpoints
- `filters`: JWT/CORS

## 5. Securite
- JWT valide dans `JWTAuthFilter`.
- Controle par role via `@RolesAllowed`.
- Verification de possession (owner checks) sur routes organizer.

## 6. Donnees seed et tests
Si la base est vide, `DataInitializer` cree:
- admins,
- organizers,
- customers,
- concerts,
- artistes,
- tickets.

Comptes de test recommandes pour evaluation:
- Organizer: `organizer0@test.xyz` / `password0`
- Customer: `customer0@test.xyz` / `password0`

## 7. Configuration et execution
- Dossier config DB: `src/main/resources/META-INF/persistence.xml`
- Scripts de lancement: `run-all.bat`, `run-all.sh`
- URLs:
  - API: `http://localhost:8080`
  - OpenAPI: `http://localhost:8080/openapi.json`
  - Swagger UI: `http://localhost:8080/api/docs`

## 8. SMTP
Le projet contient une configuration SMTP fichier:
- `src/main/resources/smtp.properties`

Le service mail lit prioritairement `SMTP_*` en variables d'environnement, sinon ce fichier.

## 9. Limites / remarques
- Peu de tests automatises actuellement.
- Quelques zones encore en evolution fonctionnelle selon front.
- Le module alerte email avance est initié sur la brache alerte du backendsans être finalisé.

## 10. Bilan
Le projet est exploitable en demo complete pour:
- authentification,
- recherche,
- achat de ticket,
- back-office organizer,
- statistiques et notifications.

