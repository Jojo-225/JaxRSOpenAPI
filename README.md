# Concert Ticketing API (JAX-RS + OpenAPI)

API REST de gestion de concerts, billetterie et utilisateurs (admin, organisateur, customer), construite avec **JAX-RS (Resteasy)**, **Hibernate/JPA**, **HSQLDB**, et documentée avec **OpenAPI/Swagger**.

## Equipe

Par :

- Fulbert SOSSA
- Eunice OYOTODE

## Objectif

Le projet couvre:

- authentification JWT (`/api/auth`),
- actions client (profil, tickets, achat),
- back-office organisateur (concerts, billets, artistes, dashboard),
- endpoints d'administration.

## Stack technique

- Java 15
- Maven
- JAX-RS / Resteasy (Jakarta)
- Hibernate JPA
- HSQLDB (local)
- Swagger/OpenAPI v3
- JWT (`jjwt`) + BCrypt

## Architecture (vue rapide)

`src/main/java/fr/istic/taa/jaxrs`:

- `domain/`: entités métier (`User`, `Organizer`, `Concert`, `Ticket`, ...)
- `dao/`: accès données JPA
- `service/`: logique applicative
- `dto/`: DTO requête/réponse + mappers
- `filters/`: sécurité JWT (`JWTAuthFilter`)
- `rest/`: ressources API (public, auth, customer actions)
- `rest/organizer/`: back-office organisateur
- `rest/manage/`: endpoints de gestion

## Démarrage rapide

Consulte le guide complet: [install.md](/c:/Users/fsossa/Desktop/M1_MIAGE/SIR/JaxRSOpenAPI/install.md)

Commandes rapides:

### Windows

```powershell
.\run-all.bat
```

### Linux/macOS

```bash
chmod +x run-all.sh
./run-all.sh
```

## URLs principales

- API: `http://localhost:8080`
- OpenAPI JSON: `http://localhost:8080/openapi.json`
- Swagger UI: `http://localhost:8080/api/docs`

## Authentification

### Login

`POST /api/auth/login`

Payload:

```json
{
  "email": "organizer0@test.xyz",
  "password": "password0"
}
```

Réponse: token JWT + rôles.

### En-tête requis

```http
Authorization: Bearer <token>
```

## Modules d'endpoints

## `Auth`

Base path: `/api/auth`

- `POST /login`
- `POST /register`
- `POST /logout`
- `GET /me`
- `GET /me/role`

## `Home`

Base path: `/api`

- `GET /latestConcerts`
- `GET /incomingConcerts`
- `GET /search`
- `GET /{id}`
- `GET /mytickets`

## `Customer Actions`

Base path: `/api/custom`

- `GET /profile`
- `GET /mytickets`
- `POST /buyticket`

Ces endpoints sont JWT-protégés.

## `Organizer Back Office`

### Dashboard

Base path: `/organise/dashboard`

- `GET /` (stats + concerts à venir + quick actions)

### Concerts

Base path: `/organise/concerts`

- `GET /`
- `GET /{id}`
- `POST /`
- `PUT /{id}`
- `DELETE /{id}`
- `GET /{id}/customers`
- `GET /upcoming`
- `GET /stats/dashboard`

### Tickets

Base path: `/organise/tickets`

- `GET /`
- `GET /{id}`
- `GET /concert/{concertId}`
- `POST /concert/{concertId}`
- `POST /`
- `PUT /{id}`
- `DELETE /{id}`

### Artists

Base path: `/organise/artists`

- `GET /`
- `GET /{id}`
- `GET /concert/{concertId}`
- `POST /concert/{concertId}`
- `POST /{artistId}/concert/{concertId}`
- `PUT /{id}`
- `DELETE /{artistId}/concert/{concertId}`

## `Manage`

- `/manage/admin`
- `/organise/organizers`

## Sécurité et rôles

Le filtre JWT (`JWTAuthFilter`) protège les routes annotées `@RolesAllowed`.

Rôles principaux:

- `ADMIN`
- `ORGANIZER`
- `CUSTOMER`

Le rôle utilisateur provient du type concret (`dtype`) et des claims JWT.

## Données de démo

Le projet initialise des données au démarrage en environnement dev:

- admins (`adminX@test.xyz`)
- organizers (`organizerX@test.xyz`)
- customers (`customerX@test.xyz`)
- concerts / artistes / tickets

Mots de passe seed usuels: `password0`, `password1`, etc.

## Configuration base de données

Fichier: `src/main/resources/META-INF/persistence.xml`

- `dev`: HSQLDB (`drop-and-create`)
- `prod`: HSQLDB (`update`)
- `mysql`: MySQL (`update`)

## Scripts utilitaires

- [run-all.bat](/c:/Users/fsossa/Desktop/M1_MIAGE/SIR/JaxRSOpenAPI/run-all.bat)
- [run-all.sh](/c:/Users/fsossa/Desktop/M1_MIAGE/SIR/JaxRSOpenAPI/run-all.sh)
- `run-hsqldb-server.bat/.sh`
- `show-hsqldb.bat/.sh`

## Vérification locale

```bash
mvn -DskipTests compile
```

## Remarques

- Le projet est en évolution active (certaines routes peuvent être affinées au fur et à mesure du front).
- Pour une clé JWT persistante entre redémarrages, définir `JWT_SECRET`.
