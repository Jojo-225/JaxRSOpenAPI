# Concert Ticketing API

API REST de gestion de concerts, billetterie et utilisateurs.
Stack principale: JAX-RS (Resteasy), Hibernate/JPA, HSQLDB, Swagger/OpenAPI, JWT.

## Equipe
- Fulbert SOSSA
- Eunice OYOTODE

## Etat actuel du projet
- Auth JWT complete (`/api/auth/*`)
- Espace public home/recherche (`/api/*`)
- Espace customer (`/api/custom/*`)
- Back-office organizer (`/organise/*`)
- Admin/manage
- Gestion des ventes de tickets (quantite, reference, historique)
- Notifications internes (base + SSE)
- SMTP configure par fichier `smtp.properties` (pour envoi mails - en cours sur branch alert)

## Identifiants de test
Ces comptes sont crees automatiquement par `DataInitializer` (si base vide):

- Organizer:
  - email: `organizer0@test.xyz`
  - mot de passe: `password0`
- Customer:
  - email: `customer0@test.xyz`
  - mot de passe: `password0`

## Lancement rapide
Voir le guide detaille: [install.md](/c:/Users/fsossa/Desktop/M1_MIAGE/SIR/JaxRSOpenAPI/install.md)

Windows:
```powershell
.\run-all.bat
```

Linux/macOS:
```bash
chmod +x run-all.sh
./run-all.sh
```

## URLs utiles
- API: `http://localhost:8080`
- OpenAPI JSON: `http://localhost:8080/openapi.json`
- Swagger UI: `http://localhost:8080/api/docs`

## Authentification
Login:
- `POST /api/auth/login`

Header pour routes protegees:
```http
Authorization: Bearer <token>
```

## Endpoints principaux

Auth (`/api/auth`)
- `POST /login`
- `POST /register`
- `POST /logout`
- `GET /me`
- `GET /me/role`

Home public (`/api`)
- `GET /latestConcerts`
- `GET /incomingConcerts`
- `GET /search`
- `GET /{id}` (concert detail)
- `GET /tickets/{id}` (ticket detail)

Customer (`/api/custom`)
- `GET /profile`
- `GET /mytickets`
- `GET /my-purchases`
- `GET /tickets/{id}`
- `POST /buyticket`
- `GET /notification-preferences`
- `PUT /notification-preferences`

Organizer (`/organise/concerts`)
- CRUD concerts
- `GET /{id}/customers`
- `GET /upcoming`
- `GET /stats/dashboard`
- `GET /sales/me`
- `GET /sales/me/history`
- `GET /sales/organizer/{organizerId}`

Organizer tickets (`/organise/tickets`)
- CRUD tickets
- `GET /concert/{concertId}`
- `GET /stats/sales`

Organizer artists (`/organise/artists`)
- CRUD + liaison artiste/concert

Notifications (`/notifications`)
- create/list/read/read-all
- stream SSE

## Configuration SMTP
Fichier a remplir:
- [smtp.properties](/c:/Users/fsossa/Desktop/M1_MIAGE/SIR/JaxRSOpenAPI/src/main/resources/smtp.properties)

`EmailService` lit d'abord les variables d'environnement `SMTP_*`, sinon `smtp.properties`.

## Verification
```bash
mvn -DskipTests compile
```
