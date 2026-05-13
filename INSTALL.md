# Installation du projet

Ce guide decrit l'installation et l'execution locale de l'API Concert Ticketing.

## 1. Prerequis
- Java 15
- Maven 3.8+
- Git

Verification:
```bash
java -version
mvn -version
```

## 2. Recuperer le projet
```bash
git clone <url-du-repo>
cd JaxRSOpenAPI
```

## 3. Base de donnees
Le projet tourne en HSQLDB serveur local.

Configuration: `src/main/resources/META-INF/persistence.xml`
- persistence unit `dev`
- creation/mise a jour schema automatique

Au demarrage, `DataInitializer`:
- cree les donnees seed si base vide,
- applique des adaptations schema necessaires (ex: colonnes tickets/ticket sales).

## 4. Lancer en une commande

### Windows
```powershell
.\run-all.bat
```

### Linux/macOS
```bash
chmod +x run-all.sh
./run-all.sh
```

## 5. Lancement manuel

### A. Dependencies
```bash
mvn dependency:copy-dependencies
```

### B. Demarrer HSQLDB
```bash
mkdir -p data
cd data
java -cp ../target/dependency/hsqldb-2.7.2.jar org.hsqldb.Server
```

### C. Ouvrir client HSQLDB (optionnel)
```bash
java -cp ../target/dependency/hsqldb-2.7.2.jar org.hsqldb.util.DatabaseManagerSwing --driver org.hsqldb.jdbcDriver --url jdbc:hsqldb:hsql://localhost/ --user SA
```

### D. Demarrer API
```bash
mvn jetty:run
```

## 6. URLs utiles
- API: `http://localhost:8080`
- OpenAPI JSON: `http://localhost:8080/openapi.json`
- Swagger UI: `http://localhost:8080/api/docs`

## 7. Identifiants de test (prof)
Comptes seed par defaut:

- Organizer
  - email: `organizer0@test.xyz`
  - mot de passe: `password0`
- Customer
  - email: `customer0@test.xyz`
  - mot de passe: `password0`

(Autres comptes seed disponibles: `organizer1..4`, `customer1..49`, mots de passe `passwordX`.)

## 8. Auth JWT
1. `POST /api/auth/login`
2. Recuperer le token
3. Ajouter:
```http
Authorization: Bearer <token>
```

Option recommande pour stabilite inter-redemarrage:
```powershell
$env:JWT_SECRET="votre-cle-jwt-stable"
```

## 9. Configuration SMTP (si envoi mail active)
Remplir:
- `src/main/resources/smtp.properties`

Champs:
- `SMTP_HOST`
- `SMTP_PORT`
- `SMTP_FROM`
- `SMTP_AUTH`
- `SMTP_USERNAME`
- `SMTP_PASSWORD`
- `SMTP_STARTTLS`

## 10. Verification
```bash
mvn -DskipTests compile
```

## 11. Depannage rapide
- Port 8080 occupe: liberer le port ou modifier `pom.xml`.
- Swagger inaccessible: verifier `/openapi.json` puis `/api/docs`.
- DB non joignable: demarrer HSQLDB avant `mvn jetty:run`.
